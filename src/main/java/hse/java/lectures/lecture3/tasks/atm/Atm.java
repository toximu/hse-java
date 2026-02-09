package hse.java.lectures.lecture3.tasks.atm;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Atm {
    private enum Denomination {
        D50(50),
        D100(100),
        D500(500),
        D1000(1000),
        D5000(5000);

        private final int value;

        Denomination(int value) {
            this.value = value;
        }

        int value() {
            return value;
        }
    }

    Integer balance = 0;

    private final Map<Denomination, Integer> banknotes = new EnumMap<>(Denomination.class);

    public Atm() {
        banknotes.put(Denomination.D50,0);
        banknotes.put(Denomination.D100,0);
        banknotes.put(Denomination.D500,0);
        banknotes.put(Denomination.D1000,0);
        banknotes.put(Denomination.D5000,0);
    }

    Denomination getDenomination (Integer v) {
        if (v == 50) {
            return Denomination.D50;
        } else if (v == 100) {
            return Denomination.D100;
        } else if (v == 500) {
            return Denomination.D500;
        } else if (v == 1000) {
            return Denomination.D1000;
        } else if (v == 5000) {
            return Denomination.D5000;
        } else {
            throw new InvalidDepositException("InvalidDepositException");
        }
    }

    Integer checkAmountBanknotes(Integer v) {
        if (v > 0) {return v;}
        throw new InvalidDepositException("InvalidDepositException");
    }

    public void deposit(Map<Integer, Integer> banknotes){
        if (banknotes == null) {throw new InvalidDepositException("");}
        for (var item : banknotes.entrySet()) {
            this.banknotes.put(getDenomination(item.getKey()), checkAmountBanknotes(item.getValue()));
            balance += item.getKey() * item.getValue();
        }
    }

    void getOutBanknotes(Denomination d, Integer amount) {
        banknotes.merge(d, -amount, Integer::sum);
        balance -= d.value() * amount;
    }

    public Map<Integer, Integer> withdraw(int amount) {
        if (amount <= 0) {throw new InvalidAmountException("InvalidAmountException");}
        if (amount > balance) {throw new InsufficientFundsException("InsufficientFundsException");}

        Map<Integer, Integer> result = new HashMap<>()


        Denomination[] denoms = {Denomination.D5000, Denomination.D1000,
        Denomination.D500, Denomination.D100, Denomination.D50};

        for (var d : denoms) {
            while (this.banknotes.get(d) > (result.containsKey(d.value()) ? result.get(d.value()) : 0) && (amount >= d.value())) {
                result.merge(d.value(), 1, Integer::sum);
                amount -= d.value();
                if (amount == 0) {
                    break;
                }
            }
        }

        if (amount == 0) {
            for (var item : result.entrySet()) {
                getOutBanknotes(getDenomination(item.getKey()), item.getValue());
            }
        } else {
            throw new CannotDispenseException("CannotDispenseException");
        }

        return result;
    }

    public int getBalance() {
        return balance;
    }

}
