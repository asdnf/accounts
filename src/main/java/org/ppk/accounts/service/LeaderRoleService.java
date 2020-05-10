package org.ppk.accounts.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.ppk.accounts.dto.SyncLease;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.Objects;

public class LeaderRoleService {

    public static final long LEASE_TIME = 20000; // 20 seconds
    public static final int NUMBER_OF_TIMES_TO_WAIT_FOR_LEADER = 10;
    public static final long SCHEDULER_DELAY = 1000;
    private static final Logger logger = LoggerFactory.getLogger(LeaderRoleService.class);
    private Phase phase = Phase.INITIAL;
    private SyncLease lastSyncedResponse;
    private int GETTING_LEADER_ID_Timer;
    @Autowired
    private ServiceIdentifier serviceIdentifier;
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private KafkaTemplate<String, SyncLease> leaseTemplate;
    @Autowired
    private SyncLeaseFactory syncLeaseFactory;
    @Autowired
    private UIDGenerator uidGenerator;
    @Autowired
    private CurrentDateGenerator currentDateGenerator;
    @Autowired
    private TransactionProcessorService transactionProcessorService;

    @Scheduled(fixedDelay = SCHEDULER_DELAY)
    public void processLeadershipNegotiationPhase() {

        logger.info("Processing phase {}", getPhase());

        if (getPhase() == Phase.INITIAL) {
            getLeaderTask();
            setPhase(Phase.GETTING_LEADER_ID, () -> {
                GETTING_LEADER_ID_Timer = 0;
            });
        }

        if (getPhase() == Phase.GETTING_LEADER_ID) {
            if (GETTING_LEADER_ID_Timer < NUMBER_OF_TIMES_TO_WAIT_FOR_LEADER) {
                GETTING_LEADER_ID_Timer++;
            } else {
                claimThisInstanceALeaderTask();
            }
        }

        if (getPhase() == Phase.GOT_LEADER_ID) {
            boolean leaseIsValid = checkLeaseIsFresh();

            if (leaseIsValid) {

            }
            String leaderId = lastSyncedResponse.getLeaderId();
            String thisId = serviceIdentifier.getIdentifier();
            // We are the leader of cluster. Thus we should process transactions.
            if (Objects.equals(leaderId, thisId)) {
                transactionProcessorService.processTransactionsAsLeader();
            }
        }
    }


    public boolean checkLeaseIsFresh() {
        Date lastLease = lastSyncedResponse.getIssueDate();
        long leaseTime = lastSyncedResponse.getLeaseTime();
        Date now = currentDateGenerator.getNow();
        if (lastLease.getTime() + leaseTime < now.getTime()) {
            setPhase(Phase.INITIAL, null);
            return false;
        }
        return true;
    }

    public synchronized void setPhase(Phase phase, Runnable phaseSwitchSynchronousAction) {
        if (phaseSwitchSynchronousAction != null) {
            phaseSwitchSynchronousAction.run();
        }
        this.phase = phase;
    }

    public synchronized Phase getPhase() {
        return phase;
    }

    @KafkaListener(topics = "lease")
    public void listenLease(ConsumerRecord<String, SyncLease> leaseRecord) {
        SyncLease syncLease = leaseRecord.value();
        if (SyncCommand.SYNC_RESPONSE.toString().equals(syncLease.getCommand())) {
            setPhase(Phase.GOT_LEADER_ID, () -> {
                if (lastSyncedResponse == null) {
                    lastSyncedResponse = syncLease;
                } else {
                    if (syncLease.getSerialId() > lastSyncedResponse.getSerialId()) {
                        lastSyncedResponse = syncLease;
                    }
                }
            });
        }

        if (SyncCommand.SYNCHRONIZE.toString().equals(syncLease.getCommand())) {
            postLastKnownLeaderLeaseTask();
        }
    }

    @Async
    public void postLastKnownLeaderLeaseTask() {
        String id = uidGenerator.getUID();
        SyncLease syncLease = syncLeaseFactory.createSyncLeaseInstance();
        syncLease.setCommand(SyncCommand.SYNC_RESPONSE.toString());
        syncLease.setIssueDate(lastSyncedResponse.getIssueDate());
        syncLease.setIssueTimeStamp(lastSyncedResponse.getIssueTimeStamp());
        syncLease.setLeaderId(lastSyncedResponse.getLeaderId());
        syncLease.setSerialId(lastSyncedResponse.getSerialId());
        leaseTemplate.send(id, syncLease);
    }

    @Async
    public void getLeaderTask() {
        String id = uidGenerator.getUID();
        SyncLease syncLease = syncLeaseFactory.createSyncLeaseInstance();
        syncLease.setCommand(SyncCommand.SYNCHRONIZE.toString());
        syncLease.setIssueDate(null);
        syncLease.setIssueTimeStamp(null);
        syncLease.setLeaderId(null);
        syncLease.setSerialId(null);
        syncLease.setLeaseTime(null);
        leaseTemplate.send(id, syncLease);
    }

    @Async
    public void claimThisInstanceALeaderTask() {
        String id = uidGenerator.getUID();
        SyncLease syncLease = syncLeaseFactory.createSyncLeaseInstance();
        Date current = currentDateGenerator.getNow();
        syncLease.setCommand(SyncCommand.SYNC_RESPONSE.toString());
        syncLease.setIssueDate(current);
        syncLease.setIssueTimeStamp(current.getTime());
        syncLease.setLeaderId(serviceIdentifier.getIdentifier());
        syncLease.setSerialId(0l);
        syncLease.setLeaseTime(LEASE_TIME);
        leaseTemplate.send(id, syncLease);
    }

    public enum Phase {
        INITIAL, GETTING_LEADER_ID, GOT_LEADER_ID
    }

    public enum SyncCommand {
        SYNCHRONIZE, SYNC_RESPONSE
    }

}
