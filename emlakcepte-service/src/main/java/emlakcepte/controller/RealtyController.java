package emlakcepte.controller;

import java.util.List;

import emlakcepte.dto.model.response.RealtyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import emlakcepte.model.Realty;
import emlakcepte.dto.model.request.RealtyRequest;
import emlakcepte.service.RealtyService;

@RestController
@RequestMapping(value = "/realties")
public class RealtyController {

	private final RealtyService realtyService;

	public RealtyController(RealtyService realtyService)
	{
		this.realtyService = realtyService;
	}
	@GetMapping
	public ResponseEntity<List<RealtyResponse>> getAll()
	{
		return ResponseEntity.ok(realtyService.getAll());
	}

	@PostMapping
	public ResponseEntity<RealtyResponse> create(@RequestBody RealtyRequest realtyRequest) throws Exception
	{
		RealtyResponse realty = realtyService.create(realtyRequest);
		return new ResponseEntity<>(realty, HttpStatus.CREATED);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<List<RealtyResponse>> getAllByUserId(@PathVariable int id)
	{
		var realtyes = realtyService.getAllById(id);
		return ResponseEntity.ok(realtyes);
	}
	
	@GetMapping(value = "/status/active")
	public ResponseEntity<List<RealtyResponse>> getAllActiveRealties()
	{
		var realties = realtyService.getAllActiveRealties();
		return ResponseEntity.ok(realties);
	}

	@DeleteMapping(value = "/{realtyId}")
	public ResponseEntity<Integer> delete(@PathVariable Integer realtyId)
	{
		realtyService.delete(realtyId);
		return ResponseEntity.ok(realtyId);
	}

	@PutMapping(value = "/{realtyId}")
	public ResponseEntity<RealtyResponse> update(@PathVariable Integer realtyId,@RequestBody RealtyRequest realtyRequest)
	{
		return new ResponseEntity<>(realtyService.update(realtyId, realtyRequest), HttpStatus.OK);
	}

	@GetMapping(value = "/getRealtyByUserId/{userId}/{rId}")
	@ResponseBody
	public ResponseEntity<RealtyResponse> getRealtyByUserId (@PathVariable Integer userId, @PathVariable Integer rId)
	{
		return new ResponseEntity<>(realtyService.getRealtyByUserId(userId, rId), HttpStatus.ACCEPTED);
	}

	@GetMapping(value = "/getRealtyByUserId/{userId}")
	public ResponseEntity<List<RealtyResponse>> getRealtyByUserId (@PathVariable Integer userId)
	{
		return new ResponseEntity<>(realtyService.getAllById(userId), HttpStatus.ACCEPTED);
	}

	@GetMapping(value = "/getAllRealtyByProvince/{province}")
	public ResponseEntity<List<RealtyResponse>> getAllRealtyByProvince (@PathVariable String province)
	{
		return new ResponseEntity<>(realtyService.getAllRealtyByProvince(province), HttpStatus.ACCEPTED);
	}

	@PutMapping("/changeStatusOfRealty/{rId}")
	public ResponseEntity<RealtyResponse> changeStatusOfRealty (@PathVariable Integer rId)
	{
		return new ResponseEntity<>(realtyService.changeStatusOfRealty(rId), HttpStatus.ACCEPTED);
	}

	@GetMapping(value = "/getActiveRealtyByUserId/{id}")
	public ResponseEntity<List<RealtyResponse>> getActiveRealtyByUserId (@PathVariable Integer id)
	{
		return new ResponseEntity<>(realtyService.getActiveRealtyByUserId(id), HttpStatus.ACCEPTED);
	}



}
