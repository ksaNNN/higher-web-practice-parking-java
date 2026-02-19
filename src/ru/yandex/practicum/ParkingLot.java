package ru.yandex.practicum;

import java.util.HashMap;
import java.util.Set;

public class ParkingLot extends AbstractParkingLot {

    private char[] spots;  // Массив мест: 'n', 'e', 'p' (свободные), 'N', 'E', 'P' (занятые)
    private HashMap<String, Integer> parkedCars;  // номер машины → индекс места
    private int normalSpots;
    private int electricSpots;
    private int premiumSpots;

    public ParkingLot(int totalSpots, int electricSpots, int premiumSpots) {
        super(totalSpots, electricSpots, premiumSpots);

        this.spots = new char[totalSpots];
        this.parkedCars = new HashMap<>();
        this.normalSpots = totalSpots - electricSpots - premiumSpots;
        this.electricSpots = electricSpots;
        this.premiumSpots = premiumSpots;

        // Заполняем массив мест
        int index = 0;

        // Обычные места
        for (int i = 0; i < normalSpots; i++) {
            spots[index++] = 'n';
        }

        // Электро места
        for (int i = 0; i < electricSpots; i++) {
            spots[index++] = 'e';
        }

        // Премиум места
        for (int i = 0; i < premiumSpots; i++) {
            spots[index++] = 'p';
        }

        System.out.println("ParkingLot created with " + totalSpots + " spots");
    }

    @Override
    public void enter(String carType, String number) throws ParkingException {
        // Проверка на null
        if (carType == null || number == null) {
            throw new ParkingException("carType or number is null");
        }

        // Проверяем что машина ещё не припаркована
        if (parkedCars.containsKey(number)) {
            throw new ParkingException("already parked");
        }

        // Определяем тип машины
        char carTypeChar = carType.charAt(0); // 'N', 'E', 'P'

        // Ищем свободное место
        int spotIndex = findAvailableSpot(carTypeChar);

        if (spotIndex == -1) {
            throw new ParkingException("no free parking spot");
        }

        // Паркуем машину
        spots[spotIndex] = Character.toUpperCase(spots[spotIndex]); // n → N, e → E, p → P
        parkedCars.put(number, spotIndex);

        System.out.println(number + " parked at " + carTypeChar + spotIndex);
    }

    @Override
    public void leave(String number) throws ParkingException {
        // Проверка на null
        if (number == null) {
            throw new ParkingException("number is null");
        }

        // Проверяем что машина припаркована
        if (!parkedCars.containsKey(number)) {
            throw new ParkingException("not parked");
        }

        // Находим место
        Integer spotIndex = parkedCars.get(number);

        if (spotIndex == null || spotIndex < 0 || spotIndex >= spots.length) {
            throw new ParkingException("spot not found");
        }

        // Освобождаем место
        char spotType = spots[spotIndex];
        spots[spotIndex] = Character.toLowerCase(spotType); // N → n, E → e, P → p
        parkedCars.remove(number);

        System.out.println(number + " leaved from " + spotType + spotIndex);
    }

    @Override
    public Set<String> getNumbers() {
        return parkedCars.keySet();
    }

    @Override
    boolean isEmpty() {
        return parkedCars.isEmpty();
    }

    /**
     * Находит свободное место для машины определённого типа
     * @param carType 'N' - обычная, 'E' - электро, 'P' - премиум
     * @return индекс свободного места или -1 если нет мест
     */
    private int findAvailableSpot(char carType) {
        if (carType == 'N') {
            // Обычная машина - только на обычное место
            return findSpot('n');
        } else if (carType == 'E') {
            // Электромобиль - только на электро место
            return findSpot('e');
        } else if (carType == 'P') {
            // Премиум машина - только на премиум место
            return findSpot('p');
        }

        return -1;
    }

    /**
     * Находит первое свободное место указанного типа
     * @param spotType 'n', 'e' или 'p'
     * @return индекс места или -1
     */
    private int findSpot(char spotType) {
        for (int i = 0; i < spots.length; i++) {
            if (spots[i] == spotType) {
                return i;
            }
        }
        return -1;
    }
}