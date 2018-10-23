package edu.temple.cis.c3238.banksim;

/**
 * @author Cay Horstmann
 * @author Modified by Paul Wolfgang
 * @author Modified by Charles Wang
 */
public class Bank {

    public static final int NTEST = 10;
    private final Account[] accounts;
    private long ntransacts = 0;
    private final int initialBalance;
    private final int numAccounts;
    private boolean open;
    private static int transactions = 0;

    public Bank(int numAccounts, int initialBalance) {
        open = true;
        this.initialBalance = initialBalance;
        this.numAccounts = numAccounts;
        accounts = new Account[numAccounts];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account(this, i, initialBalance);
        }
        ntransacts = 0;
    }

    public synchronized void transfer(int from, int to, int amount) {
        transactions++;
        //System.out.println("Transaction #" + transactions);

        //accounts[from].waitForAvailableFunds(amount);
        if (accounts[from].withdraw(amount)) {
            accounts[to].deposit(amount);
        }

        try {
            if (shouldTest()) {
                //test();
                new TestThread(this).start();
                wait();
            } else {
                wait();
            }
        } catch (InterruptedException exception) {
        }
    }

    public synchronized void test() {
        int sum = 0;
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        for (Account account : accounts) {
            System.out.printf("%s %s%n",
                    Thread.currentThread().toString(), account.toString());
            sum += account.getBalance();
        }

        System.out.println(Thread.currentThread().toString()
                + " Sum: " + sum);
        if (sum != numAccounts * initialBalance) {
            System.out.println(Thread.currentThread().toString()
                    + " Money was gained or lost");
            System.exit(1);
        } else {
            System.out.println(Thread.currentThread().toString()
                    + " The bank is in balance");
            System.out.println("Total transactions: " + ntransacts);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
        notifyAll();
    }

    public int size() {
        return accounts.length;
    }

    public boolean shouldTest() {
        return ++ntransacts % NTEST == 0;
    }

    public synchronized boolean isOpen() {
        return open;
    }

    public void closeBank() {
        synchronized (this) {
            open = false;
        }
        for (Account account : accounts) {
            synchronized (account) {
                account.notifyAll();
            }
        }
    }
}
