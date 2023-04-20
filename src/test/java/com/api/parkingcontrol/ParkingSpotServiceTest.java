package com.api.parkingcontrol;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import com.api.parkingcontrol.services.ParkingSpotService;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ParkingSpotServiceTest")
public class ParkingSpotServiceTest extends ParkingControlApplicationTests {

    @MockBean
    private ParkingSpotRepository parkingSpotRepository;
    @Autowired
    private ParkingSpotService parkingSpotService;

    @Test
    @DisplayName("Deve retornar todos os dados de parkingSpot")
    public void retornaTodosDadosDEParkingSpot(){
        Pageable pageable = PageRequest.of(0, 5);

        List<ParkingSpotModel> expectedList = createRandomParkingSpotList(5); // cria lista de elementos esperados
        Page<ParkingSpotModel> pageParkingSpot = MockParkingSpot(expectedList); // cria a página simulada com a lista esperada
        Mockito.when(parkingSpotRepository.findAll(ArgumentMatchers.eq(pageable))).thenReturn(pageParkingSpot);

        Page<ParkingSpotModel> actualPage = parkingSpotService.findAll(pageable); // chama o método a ser testado
        List<ParkingSpotModel> actualList = actualPage.getContent(); // obtém a lista de elementos da página retornada

        assertEquals(expectedList, actualList); // verifica se a lista retornada é igual à lista esperada
    }

    @Test
    @DisplayName("Deve retornar apenas um registro de parkingSpot")
    public void retornaUmRegistroDeParkingSpot(){
        List<ParkingSpotModel> expectedList = createRandomParkingSpotList(5); // cria lista de elementos esperados
        ParkingSpotModel parkingSpot = expectedList.get(expectedList.size() - 1); // cria a página simulada com a lista esperada
        Optional<ParkingSpotModel> optionalParkingSpot = Optional.ofNullable(parkingSpot); // converte parkingSpot em um Optional
        Mockito.when(parkingSpotRepository.findById(ArgumentMatchers.eq(parkingSpot.getId()))).thenReturn(optionalParkingSpot);

        Optional<ParkingSpotModel> getParkingSpot = parkingSpotService.findById(parkingSpot.getId());

        assertEquals(optionalParkingSpot, getParkingSpot);
    }

    @Test
    @DisplayName("Deve salvar um registro de parkingSpot")
    public void salvaUmRegistroDeParkingSpot(){
        Faker faker = new Faker();
        ParkingSpotModel parkingSpot = Mockito.mock(ParkingSpotModel.class);
        parkingSpot.setParkingSpotNumber(faker.regexify("[A-Z]\\d{3}"));
        parkingSpot.setLicensePlateCar(faker.regexify("[A-Z]{3}-\\d{4}"));
        parkingSpot.setBrandCar(faker.company().name());
        parkingSpot.setModelCar(faker.regexify("(Corolla|Civic|Gol|Palio|Fiesta|Sandero|Onix)"));
        parkingSpot.setColorCar(faker.color().name());
        parkingSpot.setRegistrationDate(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        parkingSpot.setResponsibleName(faker.name().fullName());
        parkingSpot.setApartment(faker.regexify("\\d{3}"));
        parkingSpot.setBlock(faker.letterify("Bloco ?"));

        Mockito.when(parkingSpotRepository.save(ArgumentMatchers.eq(parkingSpot))).thenReturn(parkingSpot);
        ParkingSpotModel getParkingSpot = parkingSpotService.save(parkingSpot);

        assertEquals(parkingSpot, getParkingSpot);
    }

    @Test
    @DisplayName("Deve atualizar um registro de parkingSpot")
    public void atualizaUmRegistroDeParkingSpot(){
        List<ParkingSpotModel> expectedList = createRandomParkingSpotList(5); // cria lista de elementos esperados
        ParkingSpotModel parkingSpot = expectedList.get(expectedList.size() - 1); // cria a página simulada com a lista esperada
        Optional<ParkingSpotModel> optionalParkingSpot = Optional.ofNullable(parkingSpot); // converte parkingSpot em um Optional
        Mockito.when(parkingSpotRepository.findById(ArgumentMatchers.eq(parkingSpot.getId()))).thenReturn(optionalParkingSpot);

        ParkingSpotModel getParkingSpot = parkingSpotService.findById(parkingSpot.getId()).get();
        Faker faker = new Faker();
        getParkingSpot.setParkingSpotNumber(faker.regexify("[A-Z]\\d{3}"));
        getParkingSpot.setLicensePlateCar(faker.regexify("[A-Z]{3}-\\d{4}"));
        getParkingSpot.setBrandCar(faker.company().name());
        getParkingSpot.setModelCar(faker.name().lastName());
        getParkingSpot.setColorCar(faker.color().name());
        getParkingSpot.setRegistrationDate(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        getParkingSpot.setResponsibleName(faker.name().fullName());
        getParkingSpot.setApartment(faker.regexify("\\d{3}"));
        getParkingSpot.setBlock(faker.letterify("Bloco ?"));

        Mockito.when(parkingSpotRepository.save(ArgumentMatchers.eq(parkingSpot))).thenReturn(parkingSpot);
        ParkingSpotModel updateParkingSpot = parkingSpotService.save(parkingSpot);

        assertEquals(updateParkingSpot, getParkingSpot);
    }

    private Page<ParkingSpotModel> MockParkingSpot(List<ParkingSpotModel> list){
        Page<ParkingSpotModel> pageParkingSpot = Mockito.mock(Page.class);
        Mockito.when(pageParkingSpot.getContent()).thenReturn(list);
        return pageParkingSpot;
    }

    public List<ParkingSpotModel> createRandomParkingSpotList(int count) {
        List<ParkingSpotModel> list = new ArrayList<>();
        Faker faker = new Faker();

        for (int i = 0; i < count; i++) {
            ParkingSpotModel parkingSpot = Mockito.mock(ParkingSpotModel.class);
            parkingSpot.setId(UUID.randomUUID());
            parkingSpot.setParkingSpotNumber(faker.regexify("[A-Z]\\d{3}"));
            parkingSpot.setLicensePlateCar(faker.regexify("[A-Z]{3}-\\d{4}"));
            parkingSpot.setBrandCar(faker.company().name());
            parkingSpot.setModelCar(faker.name().lastName());
            parkingSpot.setColorCar(faker.color().name());
            parkingSpot.setRegistrationDate(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            parkingSpot.setResponsibleName(faker.name().fullName());
            parkingSpot.setApartment(faker.regexify("\\d{3}"));
            parkingSpot.setBlock(faker.letterify("Bloco ?"));
            list.add(parkingSpot);
        }

        return list;
    }

}
