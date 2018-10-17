package edu.temple.cis.c3238.banksim;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author tub63460
 */
public class TestThread extends Thread {

    private final Bank bank;

    public TestThread(Bank b) {
        bank = b;
    }

    @Override
    public void run() {
        //try{
        bank.test();
        //}
        //catch(InterruptedException exception){
        //}
    }

}
