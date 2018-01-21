package org.mycoin.app;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.mycoin.domain.GuavaBlock;
import org.mycoin.domain.JavaBlock;
import org.mycoin.domain.Transaction;
import org.mycoin.service.JavaCoinService;

/** =================================================================================================================
 * Simple Blockchain implementation written in Java. Current block hashing choice makes use of Google's Guava
 * library but I have included a pure Java hashing implementation as well. If you wish to try the pure Java hashing
 * comment out createGuavaBlockChain in main and un-comment createJavaBlockChain.
 *
 * To Execute this application
 *      ./gradlew build
 *      ./gradlew run
 * ================================================================================================================== */
public class MainApp {
	static Logger logger = Logger.getLogger(MainApp.class);
	
	public static final String LF = System.getProperty("line.separator", "\n");
    private final static Integer numberOfBlocksToAdd = 20;
    
    // Generate a globally unique address for this node
    // TODO generate once and persist it on the disk.
    public static final String nodeId = UUID.randomUUID().toString();

    public String getGreeting() {
    	logger.info("Node ID: " + nodeId);
        String greeting = "Welcome to Java Coin. Now creating " + numberOfBlocksToAdd + " JavaCoins";
        return greeting;
    }

    void createGuavaBlockChain() {
        JavaCoinService javaCoinService = JavaCoinService.getInstance();
        List<GuavaBlock> blockChain = new ArrayList<>();
        blockChain.add(javaCoinService.createGenesisGuavaBlock());
        GuavaBlock previousBlock = blockChain.get(0);

        logger.info("Genesis Block has been added to the blockchain!" + LF + "Genesis Hash: " + blockChain.get(0).getHash() + LF);

        for (int i = 0; i < numberOfBlocksToAdd; i++) {
            final GuavaBlock newBlock = javaCoinService.nextGuavaBlock(previousBlock);
            blockChain.add(newBlock);
            previousBlock = newBlock;

            logger.info("Block #" + newBlock.getIndex() + " has been added to the blockchain! ");
            logger.info("Hash: " + newBlock.getHash() + "\n");
        }
    }

    void createJavaBlockChain() {
        JavaCoinService javaCoinService = JavaCoinService.getInstance();
        List<JavaBlock> blockChain = javaCoinService.getJavaBlockChain();
        javaCoinService.createGenesisJavaBlock();
        JavaBlock previousBlock = blockChain.get(0);

        logger.info("Genesis Block has been added to the blockchain!");
        logger.info(LF + previousBlock + LF);
        
        Random ran = new Random();

        for (int i = 0; i < numberOfBlocksToAdd; i++) {
        	javaCoinService.getTransactions().addAll(createTransactions(ran));
        	Long proof = javaCoinService.proofOfWork(previousBlock.getProof());
            final JavaBlock newBlock = javaCoinService.nextJavaBlock(previousBlock, proof);
            //blockChain.add(newBlock);
            previousBlock = newBlock;

//			logger.info("Block #" + newBlock.getIndex() + " has been added to the blockchain! ");
//			logger.info("Transactions: " + newBlock.getTransactions());
//			logger.info("Hash: " + newBlock.getHash() + LF);
            logger.info(LF + newBlock + LF);
        }
        
        JavaBlock mined = javaCoinService.mine();
        logger.info(LF + mined + LF);
        
        //JavaCoinService.printChain();
        
        boolean isChainValid = javaCoinService.validateChain(blockChain);
        logger.info("Is the chain valid? " + isChainValid);
    }
    
    private List<Transaction> createTransactions(Random ran) {
    	List<Transaction> tranList = new ArrayList<>();
    	String sender = "ojihfoi2f";
    	String recipient = "kwjhriuhkfjh";
    	double d = ran.nextDouble() * (ran.nextInt(100) + 1);
    	BigDecimal amount = BigDecimal.valueOf(d).setScale(4, BigDecimal.ROUND_HALF_EVEN);
    	Transaction tran = new Transaction(sender, recipient, amount);
    	tranList.add(tran);
    	return tranList;
    }

    public static void main(String[] args) {
        final MainApp app = new MainApp();
        logger.info(app.getGreeting());

        /* Create block hashes using Guava library */
        //app.createGuavaBlockChain();

        /* or */

        /* Create block hashes using pure Java (uncomment to use) */
        app.createJavaBlockChain();
    }
}
