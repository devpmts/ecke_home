package com.devpmts.home.services.todosender;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.MalformedParametersException;
import java.util.Optional;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devpmts.home.HomeEnv;
import com.devpmts.home.userprofile.GoogleProfile;
import com.devpmts.home.userprofile.GoogleProfileRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import lombok.SneakyThrows;
import lombok.extern.java.Log;

@Component
@RestController
@Log
public class TodoSenderService {

	@Autowired
	GoogleProfileRepository googleTokensRepo;

	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private static HttpTransport HTTP_TRANSPORT;

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}

	}

	@Autowired 
	HomeEnv homeEnv;

	@SneakyThrows
	@RequestMapping("/todo**")
	public String sendTodo(//
			@RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "notes", required = false, defaultValue = "") String notes) {
		Optional<GoogleProfile> emailSenderProfile = googleTokensRepo.emailSenderProfile();
		if (!emailSenderProfile.isPresent()) {
			return "nothing to do";
		}
		validateParameters(title, notes);
		title = computeTitle(title);
		Gmail gmail = createGmailConnection(emailSenderProfile.get());
		Message sent = sendEmail(gmail, title, notes);
		String result = title + '\n' + notes + '\n' + sent.toPrettyString();
		return result;
	}

	private void validateParameters(String title, String notes) {
		if (title.isEmpty() && notes.isEmpty()) {
			throw new MalformedParametersException("missing magic");
		}
	}

	private String computeTitle(String title) {
		if (title.isEmpty()) {
			title = "NoTitle";
		}
		return title;
	}

	private GoogleCredential createCredentialFromProfile(GoogleProfile googleProfile) {
		GoogleCredential credential = new GoogleCredential.Builder()//
				.setTransport(HTTP_TRANSPORT)//
				.setJsonFactory(JSON_FACTORY)//
				.setClientSecrets(homeEnv.CLIENT_ID, homeEnv.CLIENT_SECRET).build();//
		return credential.setAccessToken(googleProfile.getAccessToken())//
				.setRefreshToken(googleProfile.getRefreshToken());
	}

	private Message sendEmail(Gmail gmail, String title, String notes) throws IOException, MessagingException {
		Message message = createMessage(title, notes);
		return gmail.users().messages().send("me", message).execute();
	}

	private Gmail createGmailConnection(GoogleProfile emailSenderProfile) {
		GoogleCredential credential = createCredentialFromProfile(emailSenderProfile);
		Gmail gmail = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).build();
		return gmail;
	}

	private Message createMessage(String title, String notes) throws MessagingException, IOException {
		MimeMessage email = createMimeMessage(TodoEnv.TODO_EMAIL, TodoEnv.FROM_ADDRESS, title, notes);
		Message message = encodeRawMessage(email);
		return message;
	}

	private static MimeMessage createMimeMessage(String to, String from, String subject, String bodyText)
			throws MessagingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);
		email.setFrom(new InternetAddress(from));
		email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
		email.setSubject(subject);
		email.setText(bodyText);
		return email;
	}

	private static Message encodeRawMessage(MimeMessage email) throws MessagingException, IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		email.writeTo(bytes);
		String encodedEmail = Base64.encodeBase64URLSafeString(bytes.toByteArray());
		Message message = new Message();
		message.setRaw(encodedEmail);
		return message;
	}
}