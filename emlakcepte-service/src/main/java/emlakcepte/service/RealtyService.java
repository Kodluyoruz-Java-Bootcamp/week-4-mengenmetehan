package emlakcepte.service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import emlakcepte.configuration.RabbitMQConfiguration;
import emlakcepte.dto.converter.RealtyConverter;
import emlakcepte.dto.model.response.RealtyResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import emlakcepte.client.banner.Banner;
import emlakcepte.client.banner.BannerServiceClient;
import emlakcepte.controller.UserController;
import emlakcepte.model.Realty;
import emlakcepte.model.User;
import emlakcepte.model.enums.RealtyType;
import emlakcepte.model.enums.UserType;
import emlakcepte.repository.RealtyRepository;
import emlakcepte.dto.model.request.RealtyRequest;

@Service
public class RealtyService {

	private final RabbitTemplate rabbitTemplate;
	private final RabbitMQConfiguration rabbitMQConfiguration;
	private static final int MAX_INDIVIDUAL_REALTY_SIZE = 10;
	private final UserService userService;
	private final RealtyRepository realtyRepository;
	private final RealtyConverter realtyConverter;
	private final BannerServiceClient bannerServiceClient;
	private final Logger logger = Logger.getLogger(UserController.class.getName());

	public RealtyService(RabbitTemplate rabbitTemplate, RabbitMQConfiguration rabbitMQConfiguration, UserService userService, RealtyRepository realtyRepository, RealtyConverter realtyConverter, BannerServiceClient bannerServiceClient)
	{
		this.rabbitTemplate = rabbitTemplate;
		this.rabbitMQConfiguration = rabbitMQConfiguration;
		this.userService = userService;
		this.realtyRepository = realtyRepository;
		this.realtyConverter = realtyConverter;
		this.bannerServiceClient = bannerServiceClient;
	}

	public RealtyResponse create(RealtyRequest realtyRequest) throws Exception
	{
		Optional<User> foundUser = userService.getById(realtyRequest.getUserId());

		// Kullanıcı bilgileri kontrol ediliyor.
		if (foundUser.isPresent())
			if (UserType.INDIVIDUAL.equals(foundUser.get().getType()))
			{
				List<Realty> realtyList = realtyRepository.findAllByUserId(foundUser.get().getId());

				if (MAX_INDIVIDUAL_REALTY_SIZE <= realtyList.size()) {
					logger.log(Level.WARNING, "Bireysel kullanıcı en fazla 10 ilan girebilir. userID : {0}",foundUser.get().getId());

					throw new Exception("Bireysel kullanıcı en fazla 10 ilan girebilir");
				}

			}
		else
				throw new IllegalArgumentException("\"user bulunamadı\"");

		// İlan kaydediliyor.
		Realty realty = realtyConverter.convert(realtyRequest);
		realty.setUser(foundUser.get());
		Realty savedRealty = realtyRepository.save(realty);


		rabbitTemplate.convertAndSend(rabbitMQConfiguration.getrealtyQueueName(), savedRealty);


		// İlan kaydedene afiş veriyoruz.

		Banner bannerRequest = new Banner(String.valueOf(realty.getNo()), 1, "123123", "");
		Banner bannerResponse = bannerServiceClient.create(bannerRequest);


		return realtyConverter.convert(savedRealty);

	}

	public void delete(Integer id)
	{
		Optional<Realty> realty = realtyRepository.findById(id);

		realty.ifPresent(realtyRepository::delete);
	}

	public RealtyResponse update (Integer id, RealtyRequest realtyRequest )
	{
		Optional<Realty> realty = realtyRepository.findById(id);

		if (realty.isPresent()) {
			Realty realty1 = realty.get();
			realty1 = realtyConverter.convert(realtyRequest);
			return realtyConverter.convert(realtyRepository.save(realty1));
		}
		return null;
	}

	public RealtyResponse getRealtyByUserId (Integer id, Integer realtyId)
	{
		Optional<User> user = userService.getById(id);

		if (user.isPresent()) {
			return user.get()
					.getRealtyList()
					.stream().filter(r -> r.getId() == realtyId)
					.map(realtyConverter::convert).findFirst()
					.orElseThrow(IllegalArgumentException::new);
		}

		return null;
	}

	public List<RealtyResponse> getAll()
	{
		var realtyList = realtyRepository.findAll();
		return realtyList.stream().map(realtyConverter::convert)
				.collect(Collectors.toList());

	}

	public List<RealtyResponse> getAllActiveRealties()
	{
		var realtyList = realtyRepository.findAllByStatus(RealtyType.ACTIVE);
		return realtyList.stream().map(realtyConverter::convert).collect(Collectors.toList());

	}

	public List<RealtyResponse> getAllById(int id)
	{
		return realtyRepository.findAllByUserId(id)
				.stream().map(realtyConverter::convert)
				.collect(Collectors.toList());
	}

	public List<RealtyResponse> getActiveRealtyByUserId(Integer id)
	{
		return getAll().stream()
				.filter(realty -> realty.getUserId().equals(id))
				.filter(realty -> RealtyType.ACTIVE.equals(realty.getRealtyType()))
				.toList();
	}

	public List<RealtyResponse> getAllRealtyByProvince(String province)
	{
		return getAll().stream()
				.filter(realty -> realty.getProvince().equals(province))
				.collect(Collectors.toList());

	}
	public RealtyResponse changeStatusOfRealty (Integer realtyId)
	{
		var realty = realtyRepository.findById(realtyId).stream().findFirst();

		if (realty.isPresent()) {
			Realty realty1 = realty.get();
			if (RealtyType.ACTIVE.equals(realty1.getStatus()))
				realty1.setStatus(RealtyType.PASSIVE);
			else
				realty1.setStatus(RealtyType.ACTIVE);

			return realtyConverter.convert(realtyRepository.save(realty1));
		}

		throw new IllegalArgumentException();

	}
	////////











}
