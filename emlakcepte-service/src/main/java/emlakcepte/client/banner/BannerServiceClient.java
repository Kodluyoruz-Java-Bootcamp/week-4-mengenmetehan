package emlakcepte.client.banner;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "emlakcepte-banner", url = "http://localhost:8085")
public interface BannerServiceClient {

	@PostMapping(value = "/banners")
	Banner create(@RequestBody Banner banner);

}
