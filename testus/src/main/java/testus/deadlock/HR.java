package testus.deadlock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HR {

	private Lock employeesLock = new ReentrantLock();
	private Lock depLock = new ReentrantLock();
	private Map<String, String> employees = new HashMap<>();
	private int countInDep = 0;

	public void hire(String name) throws InterruptedException {
		System.out.println("Hire started.");
		employeesLock.lock();
		employees.put(name, name);
		// Thread.sleep(500);
		double x = 1;
		for (int i = 0; i < 10000; i++) {
			x = Math.cos(x);
		}
		depLock.lock();
		countInDep++;
		depLock.unlock();
		employeesLock.unlock();
		System.out.println(x);
		this.notify();
	}

	public void fire(String name) throws InterruptedException {
		this.wait();
		System.out.println("Fire started.");
		depLock.lock();
		countInDep--;
		// Thread.sleep(500);
		double x = 1;
		for (int i = 0; i < 10000; i++) {
			x = Math.cos(x);
		}
		employeesLock.lock();
		employees.remove(name);
		employeesLock.unlock();
		depLock.unlock();
		System.out.println(x);
	}

	public static void main(String[] args) {
		HR hr = new HR();

		(new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					hr.hire("Peter");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		})).start();

		(new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					hr.fire("Peter");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		})).start();

	}
}
