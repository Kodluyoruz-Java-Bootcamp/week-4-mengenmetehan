package emlakcepte.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


import emlakcepte.client.logger.LoggerServiceClient;
import emlakcepte.model.Payment;
import emlakcepte.client.payment.PaymentServiceClient;
import emlakcepte.configuration.RabbitMQConfiguration;
import emlakcepte.controller.UserController;
import emlakcepte.dto.converter.UserConverter;
import emlakcepte.model.Realty;
import emlakcepte.model.User;
import emlakcepte.model.enums.RealtyType;
import emlakcepte.repository.RealtyRepository;
import emlakcepte.repository.UserRepository;
import emlakcepte.dto.model.request.UserRequest;
import emlakcepte.dto.model.response.UserResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class UserService {

	private final RabbitTemplate rabbitTemplate;
	private final RabbitMQConfiguration rabbitMQConfiguration;
	private final UserRepository userRepository;
	private final UserConverter converter;
	private final Logger logger = Logger.getLogger(UserController.class.getName());

	private final PaymentServiceClient m_paymentServiceClient;
	private final LoggerServiceClient m_loggerServiceClient;
	private final RealtyRepository realtyRepository;


	public UserService(RabbitTemplate rabbitTemplate, RabbitMQConfiguration rabbitMQConfiguration, UserRepository userRepository, UserConverter converter, PaymentServiceClient paymentServiceClient, LoggerServiceClient loggerServiceClient, RealtyRepository realtyRepository)
	{
		this.rabbitTemplate = rabbitTemplate;
		this.rabbitMQConfiguration = rabbitMQConfiguration;
		this.userRepository = userRepository;
		this.converter = converter;
		m_paymentServiceClient = paymentServiceClient;
		m_loggerServiceClient = loggerServiceClient;
		this.realtyRepository = realtyRepository;
	}


	public UserResponse createUser(UserRequest userRequest) {
		User savedUser = userRepository.save(converter.convert(userRequest));
		logger.log(Level.INFO, "[createUser] - user created: {0}", savedUser.getId());
		rabbitTemplate.convertAndSend(rabbitMQConfiguration.getQueueName(), savedUser);

		logger.log(Level.WARNING, "[createUser] - userRequest: {0}, sent to : {1}",
				new Object[] { userRequest.getEmail(), rabbitMQConfiguration.getQueueName() });


		m_loggerServiceClient.create((long)savedUser.getId(), "priority1",
				String.format("%s - %s ", savedUser.getName(), savedUser.getEmail()));

		System.out.println((long)savedUser.getId() +
				String.format("%s - %s ", savedUser.getName(), savedUser.getEmail()));




		return converter.convert(savedUser);
	}


	public List<UserResponse> getAll() {
		return converter.convert(userRepository.findAll());
	}


	public void updatePassword(User user, String newPassword) {
		// önce hangi user bul ve passwordü update et.
	}

	public User getByEmailAntiPattern(String email) {

		//// @formatter:off
		return userRepository.findAll()
				.stream()
				.filter(user -> user.getEmail().equals(email))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
		// @formatter:on

	}

	public UserResponse getByEmail(String email) {
		return converter.convert(userRepository.findByEmail(email));
	}

	public Optional<User> getById(Integer userId) {
		return userRepository.findById(userId);
	}

	public UserResponse update (Integer id, User user)
	{
		Optional<User> user1 = userRepository.findById(id);

		if (user1.isPresent()) {
			User us = user1.get();
			us = user;              //converter.convert(userRequest);
			return converter.convert(userRepository.save(us));
		}
		return null;
	}

	public Payment processPayment(User user, String cardNo, Integer amount) throws Exception {
		return m_paymentServiceClient.processPayment(user.getId(),cardNo, amount);
	}

	public void extendPackageExpiration(Payment payment)
	{
		var user = userRepository.findById(payment.getUserId());
		if(user.isPresent()) {
			if (user.get().getPackageExpireDate() == null || user.get().getPackageExpireDate().isBefore(LocalDateTime.now())) {
				//user.get().setPackageExpireDate(LocalDateTime.now().plusMonths(1));
				user.get().setPackageExpireDate(LocalDateTime.now().plusMinutes(2));
			}
			else {
				user.get().setPackageExpireDate(user.get().getPackageExpireDate().plusMonths(1));
			}
			userRepository.save(user.get());
		}

	}

	public String login (UserRequest userRequest)
	{
		var user= userRepository.findByEmail(userRequest.getEmail());

		if (Objects.nonNull(user)) {
			String info = String.format("%s - %s isimli kullanıcı %s'de siteye giriş yaptı ",
					userRequest.getName(), userRequest.getEmail(),
					LocalDateTime.now());
			m_loggerServiceClient.create((long)user.getId(), "priority2",
					info);

			return info;
		}
		else
			throw new IllegalArgumentException("Kullanıcı bilgileri yanlış");


	}

	@PostConstruct
	public void checkPackageExpiration ()
	{
		var timer = new Timer();
		long period = 24 * 60 * 60 * 1000;


		timer.scheduleAtFixedRate(new TimerTask() {
			public void run()
			{
				System.out.println("Aktif kullanıcıların üyelikleri kontrol ediliyor " + LocalDateTime.now().toString());

				var list = userRepository.findAll().stream()
						.filter(u -> u.getPackageExpireDate() != null)
						.filter(u -> LocalDateTime.now().isAfter(u.getPackageExpireDate()))
						.toList()
						.stream()
						.map(User::getRealtyList)
						.flatMap(Collection::stream)
						.toList();

				list.forEach(r -> r.setStatus(RealtyType.PASSIVE));

				realtyRepository.saveAll(list);
			}
		}, 1000, period);

	}

}
