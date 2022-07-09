package com.rudenko.scheduler.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.time.LocalDate;

@SpringBootApplication
public class SchedulerApplication {
	public static final String FILE_NAME = "_scheduler.txt";
	public static final int MINUTE = 60000;
	public static String fileName;

	@Value("${time}")
	public String time;
	private static String TIME_STATIC;

	public static void main(String[] args) {
		SpringApplication.run(SchedulerApplication.class, args);
		try {
			System.out.println("TIME_STATIC == > " + TIME_STATIC);
			Thread.sleep(MINUTE * 2);
			fileName = LocalDate.now() + FILE_NAME;

			if (!new File(fileName).exists()) {
				createAndSave();
			}

			int dalay = MINUTE * 10;
			boolean life = true;

			while (life) {
				String read = read();
				String[] times = read.split(":");
				int timeTo = Integer.parseInt(times[0]);
				int timeNow = Integer.parseInt(times[1]);

				if (timeTo > timeNow) {
					String timeToWriteFormated = String.format("%s:%s", timeTo, timeNow + dalay);
					System.out.println("timeToWriteFormated = " + timeToWriteFormated);
					save(timeToWriteFormated);
					Thread.sleep(dalay);
				} else {
					life = false;
					Runtime.getRuntime().exec(new String[]{"shutdown", "-s"});
				}
			}
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

	@Value("${time}")
	public void setNameStatic(String time){
		TIME_STATIC = time;
	}

	private static String read() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		return reader.readLine();
	}

	private static void save(String time) throws IOException {
		FileWriter writer = new FileWriter(fileName);
		writer.write(time);
		writer.close();
	}

	private static void createAndSave() throws IOException {
		int hours = Integer.parseInt(TIME_STATIC);
		FileWriter writer = new FileWriter(fileName);
		writer.write(String.format("%s:%s", (MINUTE * 60 * hours), 0));
		writer.close();
	}

}
