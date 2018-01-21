package org.mycoin.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.validator.routines.UrlValidator;
import org.apache.log4j.Logger;
import org.mycoin.app.MainApp;
import org.mycoin.domain.GuavaBlock;
import org.mycoin.domain.JavaBlock;
import org.mycoin.domain.Transaction;
import org.mycoin.util.Helper;

public class JavaCoinService {
	static Logger logger = Logger.getLogger(JavaCoinService.class);
	
	// TODO store the chain in persistent storage.
	private List<JavaBlock> javaBlockChain;
	
	private final Set<String> nodes = new LinkedHashSet<>();
	
	private List<Transaction> transactions;
	private JavaBlock lastJavaBlock;
	
	private String tmsFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	
	private JavaCoinService() {}
	
	public static JavaCoinService getInstance() {
		return new JavaCoinService();
	}

	/**
	 * Create the genesis block
	 * @return the genesis block
	 */
    public synchronized JavaBlock createGenesisJavaBlock() {
        final String now = new SimpleDateFormat(tmsFormat).format(new Date());
        lastJavaBlock = new JavaBlock(0, now, "Genesis Block", "1", 13L, getTransactions());
        setTransactions(null);
        getJavaBlockChain().clear();
        getJavaBlockChain().add(lastJavaBlock);
        return lastJavaBlock;
    }

    public synchronized JavaBlock nextJavaBlock(final JavaBlock previousBlock, Long proof) {
        final Integer index = previousBlock.getIndex() + 1;
        final String timestamp = new SimpleDateFormat(tmsFormat).format(new Date());
        final String data = "Hey! Im block " + index.toString();
        final String previousHash = previousBlock.getHash();

        lastJavaBlock = new JavaBlock(index, timestamp, data, previousHash, proof, getTransactions());
        setTransactions(null);
        getJavaBlockChain().add(lastJavaBlock);
        return lastJavaBlock;
    }
    
    /**
	 * Creates a new transaction to go into the next mined Block.
	 * 
	 * @param transaction
	 * @return The index of the Block that will hold this transaction
	 */
    public synchronized Integer newTransaction(Transaction transaction) {
    	getTransactions().add(transaction);
    	Integer newIdx = getLastJavaBlock().getIndex() + 1;
    	logger.info("Transaction will be added to Block: " + newIdx);
    	return newIdx;
    }
    
    /**
     * Simple Proof of Work Algorithm:
         - Find a number p' such that hash(pp') contains leading 4 zeroes, where p is the previous p'
         - p is the previous proof, and p' is the new proof
     * @param lastProof
     * @return proof
     */
    public Long proofOfWork(Long lastProof) {
    	long proof = lastProof + 1;
    	while (true) {
    		if (isProofValid(lastProof, proof)) {
    			break;
    		}
    		else {
    			proof++;
    		}
    	}
    	return proof;
    }
    
    public static boolean isProofValid(long lastProof, long proof) {
    	long newProof = proof * lastProof;
		String hash = Helper.hash("" + newProof);
		return hash.startsWith("0000");
    }
    
    public JavaBlock mine() {
    	// We run the proof of work algorithm to get the next proof...
    	Long lastProof = lastJavaBlock.getProof();
    	Long proof = proofOfWork(lastProof);
    	
    	// We must receive a reward for finding the proof.
    	//  The sender is "0" to signify that this node has mined a new coin.
    	Transaction transaction = new Transaction("0", MainApp.nodeId, BigDecimal.valueOf(1L));
    	getTransactions().add(transaction);
    	
    	// Forge the new Block by adding it to the chain
    	JavaBlock newBlock = nextJavaBlock(lastJavaBlock, proof);
    	
    	return newBlock;
    }
    
	/**
	 * Determine if a given block chain is valid
	 * 
	 * @return true if valid, false if not
	 */
    public boolean validateChain(List<JavaBlock> chain) {
    	if (chain == null || chain.isEmpty() || chain.size() == 1) {
    		return true;
    	}
    	JavaBlock lastBlock = chain.get(0);
    	for (int i = 1; i < chain.size(); i++) {
    		JavaBlock currentBlock = chain.get(i);
    		//logger.info("Last block   : " + lastBlock);
    		//logger.info("Current block: " + currentBlock);
    		if (!lastBlock.hashBlock().equals(currentBlock.getPreviousHash())) {
    			return false;
    		}
    		if (!isProofValid(lastBlock.getProof(), currentBlock.getProof())) {
    			return false;
    		}
    		lastBlock = currentBlock;
    	}
    	return true;
    }
    
	/**
	 * Add a new node to the list of nodes
	 * 
	 * @param address:
	 *            address of node. example: http://192.168.0.5:5000
	 * @return true if node is registered, false if not
	 */
    public boolean registerNode(String address) {
    	boolean isValid = UrlValidator.getInstance().isValid(address);
    	if (isValid) {
    		nodes.add(address);
    		return true;
    	}
    	else {
    		logger.warn("Invalid Node Address: " + address);
    		return false;
    	}
    }
    
	/**
	 * This is our consensus algorithm, it resolves conflicts by replacing our
	 * chain with the longest one in the network.
	 * 
	 * @return true if our chain was replaced, false if not
	 */
    public boolean resolveConflicts() {
    	
    	return false;
    }
    
    public String nextAddress() {
    	return "jwohfouiweyowiqyr";
    }
    
    public void printChain() {
		logger.info(MainApp.LF + MainApp.LF
				+ "============================== Block chain ==============================" + MainApp.LF);
    	for (JavaBlock block : getJavaBlockChain()) {
    		System.out.println(block);
    	}
    }

    public GuavaBlock createGenesisGuavaBlock() {
        final String now = new SimpleDateFormat(tmsFormat).format(new Date());
        return new GuavaBlock(0, now, "Genesis Block", "0");
    }

    public GuavaBlock nextGuavaBlock(final GuavaBlock previousBlock) {
        final Integer currentIndex = previousBlock.getIndex() + 1;
        final String currentTimestamp = new SimpleDateFormat(tmsFormat).format(new Date());
        final String currentData = "Hey! Im block " + currentIndex.toString();
        final String currentHash = previousBlock.getHash();

        return new GuavaBlock(currentIndex, currentTimestamp, currentData, currentHash);
    }

    public List<JavaBlock> getJavaBlockChain() {
    	if (javaBlockChain == null) {
    		javaBlockChain = new ArrayList<>();
    	}
    	return javaBlockChain;
    }
    
	public List<Transaction> getTransactions() {
		if (transactions == null) {
			transactions = new ArrayList<>();
		}
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public JavaBlock getLastJavaBlock() {
		return lastJavaBlock;
	}

	public void setLastJavaBlock(JavaBlock lastBlock) {
		this.lastJavaBlock = lastBlock;
	}
	
	public static void main(String[] args) {
		
		try {
			JavaCoinService service = JavaCoinService.getInstance();
			
			Long proof = service.proofOfWork(21L);
			logger.info("Proof found: " + proof);
		}
		catch (Exception e) {
			logger.error("Exception caught", e);
		}
	}

}
