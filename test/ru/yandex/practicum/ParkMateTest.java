package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParkMateTest {

    @Test
    void testParkingLotCreation() {
        // Тест что парковка создаётся без ошибок
        assertDoesNotThrow(() -> {
            ParkingLot parkingLot = new ParkingLot(20, 5, 5);
            assertNotNull(parkingLot, "Парковка должна быть создана");
        });
    }

    @Test
    void testParkingLotWithDifferentSizes() {
        // Тест что парковка работает с разными размерами
        assertDoesNotThrow(() -> {
            ParkingLot small = new ParkingLot(10, 2, 2);
            ParkingLot medium = new ParkingLot(50, 10, 10);
            ParkingLot large = new ParkingLot(100, 20, 20);

            assertNotNull(small);
            assertNotNull(medium);
            assertNotNull(large);
        });
    }

    @Test
    void testMultipleEnterAndLeave() throws ParkingException {
        // Тест множественных операций
        ParkingLot parkingLot = new ParkingLot(10, 2, 2);

        // Паркуем несколько машин
        parkingLot.enter("NORMAL", "A001BC");
        parkingLot.enter("ELECTRIC", "E002KM");
        parkingLot.enter("PREMIUM", "P003AB");

        assertEquals(3, parkingLot.getNumbers().size(), "Должно быть 3 машины");

        // Одна уезжает
        parkingLot.leave("A001BC");
        assertEquals(2, parkingLot.getNumbers().size(), "Должно остаться 2 машины");

        // Приезжает новая
        parkingLot.enter("NORMAL", "A004BC");
        assertEquals(3, parkingLot.getNumbers().size(), "Снова должно быть 3 машины");
    }

    @Test
    void testExceptionHandling() {
        ParkingLot parkingLot = new ParkingLot(5, 1, 1);

        // Тест на дубликат
        assertDoesNotThrow(() -> parkingLot.enter("NORMAL", "A123BC"));
        assertThrows(ParkingException.class, () -> parkingLot.enter("NORMAL", "A123BC"));

        // Тест на отсутствующую машину
        assertThrows(ParkingException.class, () -> parkingLot.leave("Z999ZZ"));
    }

    @Test
    void testEmptyParking() {
        ParkingLot parkingLot = new ParkingLot(10, 2, 2);

        assertTrue(parkingLot.isEmpty(), "Новая парковка должна быть пуста");
        assertEquals(0, parkingLot.getNumbers().size(), "Не должно быть машин");
    }

    @Test
    void testFullParking() throws ParkingException {
        // Маленькая парковка
        ParkingLot parkingLot = new ParkingLot(3, 0, 0);

        // Заполняем
        parkingLot.enter("NORMAL", "A001BC");
        parkingLot.enter("NORMAL", "A002BC");
        parkingLot.enter("NORMAL", "A003BC");

        // Пытаемся добавить ещё
        assertThrows(ParkingException.class, () -> {
            parkingLot.enter("NORMAL", "A004BC");
        }, "Должна быть ошибка - нет мест");
    }

    @Test
    void testGetNumbers() throws ParkingException {
        ParkingLot parkingLot = new ParkingLot(10, 2, 2);

        parkingLot.enter("NORMAL", "A123BC");
        parkingLot.enter("ELECTRIC", "E456KM");

        var numbers = parkingLot.getNumbers();
        assertTrue(numbers.contains("A123BC"), "Должен быть номер A123BC");
        assertTrue(numbers.contains("E456KM"), "Должен быть номер E456KM");
        assertEquals(2, numbers.size(), "Должно быть 2 номера");
    }

    @Test
    void testCarTypesIsolation() throws ParkingException {
        ParkingLot parkingLot = new ParkingLot(6, 2, 2);

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

    @Test
    void testPremiumCarOnlyOnPremiumSpot() throws ParkingException {
        // Парковка только с премиум местами
        ParkingLot parkingLot = new ParkingLot(2, 0, 2);

        parkingLot.enter("PREMIUM", "P001AB");
        parkingLot.enter("PREMIUM", "P002AB");

        assertEquals(2, parkingLot.getNumbers().size(), "Обе премиум машины припаркованы");

        // Третья не может припарковаться
        assertThrows(ParkingException.class, () -> {
            parkingLot.enter("PREMIUM", "P003AB");
        });
    }
}