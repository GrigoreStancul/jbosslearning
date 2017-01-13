package com.packtpub.wflydevelopment.chapter7.webservice;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.json.JsonObject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;

import com.packtpub.wflydevelopment.chapter7.boundary.AccountDto;
import com.packtpub.wflydevelopment.chapter7.boundary.SeatDto;

public class RestServiceTestApplication {
	private static final String APPLICATION_URL = "http://localhost:8080/ticket-agency-ws/rest/";

	private final WebTarget accountResource;
	final Client restclient = ClientBuilder.newClient();

	public static void main(String[] args) throws InterruptedException {
		new RestServiceTestApplication().runSample();
	}

	public RestServiceTestApplication() {

		accountResource = restclient.target(APPLICATION_URL + "account");
	}

	public void runSample() throws InterruptedException {

		printAccountStatusFromServer();

		printSeatsFromServer();
		System.out.println("=== Current status: ");
		final Collection<SeatDto> seats = getSeatsFromServer();
		printSeats(seats);

		System.out.println("=== Booking: ");
		bookSeats(seats);

		System.out.println("=== Status after booking: ");
		final Collection<SeatDto> bookedSeats = getSeatsFromServer();
		printSeats(bookedSeats);

		printAccountStatusFromServer();

		Thread.sleep(5000);

	}

	private void printAccountStatusFromServer() {
		final AccountDto account = accountResource.request().get(AccountDto.class);
		System.out.println(account);
	}

	private Collection<SeatDto> getSeatsFromServer() {
		final Client restclient = ClientBuilder.newClient();
		final WebTarget seatResource;
		seatResource = restclient.target(APPLICATION_URL + "seat");
		return seatResource.request().get(new GenericType<Collection<SeatDto>>() {
		});
	}

	private void printSeatsFromServer() {
		final Client restclient = ClientBuilder.newClient();
		WebTarget seatResource = restclient.target(APPLICATION_URL + "seat");
		Future<Collection<SeatDto>> future = seatResource.request().async().get(new GenericType<Collection<SeatDto>>() {
		});
		CompletableFuture.supplyAsync(() -> {
			try {
				return future.get();
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalArgumentException(e);
			}
		}).thenAccept(
				seats -> seats.forEach(seat -> System.out.println("[" + Thread.currentThread().getId() + "] " + seat)));
	}

	private void printSeats(Collection<SeatDto> seats) {
		seats.forEach(System.out::println);
	}

	private void bookSeats(Collection<SeatDto> seats) {

		boolean synchronous = false;
		for (SeatDto seat : seats) {
			final String idOfSeat = Integer.toString(seat.getId());

			if (synchronous) {
				try {
					final Client restclient = ClientBuilder.newClient();
					final WebTarget seatResource;
					seatResource = restclient.target(APPLICATION_URL + "seat");
					seatResource.path(idOfSeat).request().post(Entity.json(""), String.class);
					System.out.println(seat + " booked");
				} catch (WebApplicationException e) {
					final Response response = e.getResponse();
					StatusType statusInfo = response.getStatusInfo();
					System.out.println(seat + " not booked (" + statusInfo.getReasonPhrase() + "): "
							+ response.readEntity(JsonObject.class).getString("entity"));
				}
			} else {
				final Client restclient = ClientBuilder.newClient();
				final WebTarget seatResource;
				seatResource = restclient.target(APPLICATION_URL + "seat");
				Future<String> post = seatResource.path(idOfSeat).request().async().post(Entity.json(""), String.class);
				CompletableFuture.supplyAsync(() -> {
					System.out.println("[" + Thread.currentThread().getId() + "] " + "book seat " + seat);
					try {
						return post.get();
					} catch (Exception e) {
						return e.getMessage();
					}
				}).thenAccept(r -> {
					System.out.println("[" + Thread.currentThread().getId() + "] " + r);
					System.out.println("[" + Thread.currentThread().getId() + "] " + seat + " booked");
				});
			}

		}
	}

	public String sendMsg() {
		return "Hello world!";
	}
}
