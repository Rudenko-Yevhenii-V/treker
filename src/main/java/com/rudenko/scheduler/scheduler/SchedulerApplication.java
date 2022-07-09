package com.rudenko.scheduler.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@SpringBootApplication
public class SchedulerApplication {
	public static final String FILE_NAME = "scheduler.txt";
	public static final int MINUTE = 60000;
	public static String fileName;

	public static void main(String[] args) {
		SpringApplication.run(SchedulerApplication.class, args);
		try {
			Thread.sleep(MINUTE * 2);

			fileName = LocalDate.now() + FILE_NAME;

			if (!new File(fileName).exists()) {
				createAndSave();
			}
			String read = read();

			int dalay = MINUTE * 10;
			boolean life = true;

			if (!(Instant.parse(read).atZone(ZoneId.systemDefault()).getDayOfWeek()
					.equals(Instant.now().atZone(ZoneId.systemDefault()).getDayOfWeek())
			)) {
				createAndSave();
			}

			while (life) {
				String[] times = read.split(":");
				int timeTo = Integer.parseInt(times[0]);
				int timeNow = Integer.parseInt(times[1]);

				if (timeTo > timeNow) {
					save(String.format("%s:%s", timeTo, timeNow + dalay));
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
		FileWriter writer = new FileWriter(fileName);
		writer.write(String.format("%s:%s", (MINUTE * 60 * 3), 0));
		writer.close();
	}
}
