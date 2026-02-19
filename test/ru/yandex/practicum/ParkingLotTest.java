package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ParkingLotTest {

    private ParkingLot parkingLot;

    @BeforeEach
    void setUp() {
        // Создаём парковку: 5 обычных, 2 электро, 2 премиум
        parkingLot = new ParkingLot(9, 2, 2);
    }

    @Test
    void testEnterNormalCar() throws ParkingException {
        parkingLot.enter("NORMAL", "A123BC");

        Set<String> numbers = parkingLot.getNumbers();
        assertTrue(numbers.contains("A123BC"), "Машина должна быть припаркована");
        assertEquals(1, numbers.size(), "Должна быть одна машина");
    }

    @Test
    void testEnterElectricCar() throws ParkingException {
        parkingLot.enter("ELECTRIC", "E456KM");

        Set<String> numbers = parkingLot.getNumbers();
        assertTrue(numbers.contains("E456KM"), "Электромобиль должен быть припаркован");
    }

    @Test
    void testEnterPremiumCar() throws ParkingException {
        parkingLot.enter("PREMIUM", "P789AB");

        Set<String> numbers = parkingLot.getNumbers();
        assertTrue(numbers.contains("P789AB"), "Премиум машина должна быть припаркована");
    }

    @Test
    void testLeaveCar() throws ParkingException {
        parkingLot.enter("NORMAL", "A123BC");
        parkingLot.leave("A123BC");

        Set<String> numbers = parkingLot.getNumbers();
        assertFalse(numbers.contains("A123BC"), "Машина должна уехать");
        assertEquals(0, numbers.size(), "Парковка должна быть пуста");
    }

    @Test
    void testEnterAlreadyParked() throws ParkingException {
        parkingLot.enter("NORMAL", "A123BC");

        ParkingException exception = assertThrows(ParkingException.class, () -> {
            parkingLot.enter("NORMAL", "A123BC");
        });

        assertEquals("already parked", exception.getMessage());
    }

    @Test
    void testLeaveNotParked() {
        ParkingException exception = assertThrows(ParkingException.class, () -> {
            parkingLot.leave("A123BC");
        });

        assertEquals("not parked", exception.getMessage());
    }

    @Test
    void testNoFreeSpots() throws ParkingException {
        // Заполняем все обычные места (5 штук)
        parkingLot.enter("NORMAL", "A001BC");
        parkingLot.enter("NORMAL", "A002BC");
        parkingLot.enter("NORMAL", "A003BC");
        parkingLot.enter("NORMAL", "A004BC");
        parkingLot.enter("NORMAL", "A005BC");

        // Пытаемся припарковать ещё одну
        ParkingException exception = assertThrows(ParkingException.class, () -> {
            parkingLot.enter("NORMAL", "A006BC");
        });

        assertEquals("no free parking spot", exception.getMessage());
    }

    @Test
    void testPremiumOnlyOnPremiumSpot() throws ParkingException {
        // Парковка с 1 премиум и 2 обычными местами
        ParkingLot parkingLot = new ParkingLot(3, 0, 1);

        // Первая премиум машина занимает премиум место
        parkingLot.enter("PREMIUM", "P001AB");
        assertEquals(1, parkingLot.getNumbers().size());

        // Вторая премиум машина НЕ может припарковаться на обычное место
        assertThrows(ParkingException.class, () -> {
            parkingLot.enter("PREMIUM", "P002AB");
        }, "Должна быть ошибка - нет премиум мест");
    }

    @Test
    void testIsEmpty() throws ParkingException {
        assertTrue(parkingLot.isEmpty(), "Парковка должна быть пуста");

        parkingLot.enter("NORMAL", "A123BC");
        assertFalse(parkingLot.isEmpty(), "Парковка не должна быть пуста");

        parkingLot.leave("A123BC");
        assertTrue(parkingLot.isEmpty(), "Парковка должна быть пуста после отъезда");
    }

    @Test
    void testMultipleCars() throws ParkingException {
        parkingLot.enter("NORMAL", "A123BC");
        parkingLot.enter("ELECTRIC", "E456KM");
        parkingLot.enter("PREMIUM", "P789AB");

        Set<String> numbers = parkingLot.getNumbers();
        assertEquals(3, numbers.size(), "Должно быть 3 машины");

        parkingLot.leave("E456KM");
        assertEquals(2, numbers.size(), "Должно остаться 2 машины");
    }

    @Test
    void testNullCarType() {
        ParkingException exception = assertThrows(ParkingException.class, () -> {
            parkingLot.enter(null, "A123BC");
        });

        assertEquals("carType or number is null", exception.getMessage());
    }

    @Test
    void testNullNumber() {
        ParkingException exception = assertThrows(ParkingException.class, () -> {
            parkingLot.enter("NORMAL", null);
        });

        assertEquals("carType or number is null", exception.getMessage());
    }

    @Test
    void testCarTypesIsolation() throws ParkingException {
        // Обычная машина
        parkingLot.enter("NORMAL", "N001BC");
        assertTrue(parkingLot.getNumbers().contains("N001BC"));

        // Электро машина
        parkingLot.enter("ELECTRIC", "E001KM");
        assertTrue(parkingLot.getNumbers().contains("E001KM"));

        // Премиум машина
        parkingLot.enter("PREMIUM", "P001AB");
        assertTrue(parkingLot.getNumbers().contains("P001AB"));

        assertEquals(3, parkingLot.getNumbers().size());
    }
}