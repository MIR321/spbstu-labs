package ru.spbstu.telematics.lab3;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Test;

public class ConcertHallTest {

	
	public boolean checkTickets(int amount) {
		HashSet<Integer> ticketsID = new HashSet<Integer>();
		for(int i = 0; 
				i < Client.purchasedTickets.size();
				i++) {
			ticketsID.add(Client.purchasedTickets.get(i).getId());
		}
		//Проверка на повтор
		if(ticketsID.size() == Client.purchasedTickets.size() 
				&& Client.purchasedTickets.size() == amount)
			return true;
		else
			return false;
	}
	
	@Test
	public void testOnSmallNumberOfClients() throws InterruptedException {
		System.out.println("TEST 3 Terminals, 10 Clients, 5 Tickets---------------");
		ConcertHall.launch(3, 10, 5);
		Assert.assertTrue(Client.leftClients == 10);
		Assert.assertTrue(checkTickets(5));
		System.out.println("-------------------------------------------------------");
		System.out.println("TEST 1 Terminals, 11 Clients, 3 Tickets---------------");
		ConcertHall.launch(1, 11, 3);
		Assert.assertTrue(Client.leftClients == 11);
		Assert.assertTrue(checkTickets(3));
		System.out.println("-------------------------------------------------------");
		System.out.println("-------------------------------------------------------");
		System.out.println("TEST 10 Terminals, 5 Clients, 100 Tickets---------------");
		ConcertHall.launch(10, 5, 100);
		Assert.assertTrue(Client.leftClients == 5);
		Assert.assertTrue(checkTickets(5));
		System.out.println("-------------------------------------------------------");
		System.out.println("TEST 100 Terminals, 1 Clients, 1000 Tickets---------------");
		ConcertHall.launch(100, 1, 1000);
		Assert.assertTrue(Client.leftClients == 1);
		Assert.assertTrue(checkTickets(1));
		System.out.println("-------------------------------------------------------");
	}
	
	@Test
	public void testDeadLock() throws InterruptedException {
		System.out.println("TEST 20 Terminals, 250 Clients, 30 Tickets---------------");
		ConcertHall.launch(20, 250, 30);
		Assert.assertTrue(Client.leftClients == 250);
		Assert.assertTrue(checkTickets(30));
		System.out.println("-------------------------------------------------------");
		
	}
	

	
}
