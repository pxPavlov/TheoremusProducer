package com.kafkajava;

import com.kafkajava.configuration.ProducerConfiguration;
import com.kafkajava.producer.LineProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
public class PpTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(PpTaskApplication.class, args);
	}

	@Bean
	public String readFile(LineProducer lineProducer) throws IOException {
		Properties properties = ProducerConfiguration.getAppProperties();
		BufferedReader reader = new BufferedReader(new FileReader("resources/raw_gps_data.csv"));
		String line = null;
		int counter = 0;

		while ((line = reader.readLine()) != null) {
			counter++;
			if(counter == 1) {
				continue;
			} else if(counter == 10) {
				break;
			}

			lineProducer.sendMessage(line, properties);
		}

		return line;
	}



}
