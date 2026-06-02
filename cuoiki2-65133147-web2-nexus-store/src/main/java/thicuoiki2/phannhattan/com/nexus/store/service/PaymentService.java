package thicuoiki2.phannhattan.com.nexus.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thicuoiki2.phannhattan.com.nexus.store.entity.PaymentBank;
import thicuoiki2.phannhattan.com.nexus.store.entity.PaymentCard;
import thicuoiki2.phannhattan.com.nexus.store.entity.User;
import thicuoiki2.phannhattan.com.nexus.store.repository.PaymentBankRepository;
import thicuoiki2.phannhattan.com.nexus.store.repository.PaymentCardRepository;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentCardRepository cardRepository;

    @Autowired
    private PaymentBankRepository bankRepository;

    public PaymentCard saveCard(
            Integer cardId,
            User user,
            String cardHolder,
            String cardNumber,
            String cardExpiry,
            String cardNetwork) {

        PaymentCard card;

        // Update
        if (cardId != null) {

            card = cardRepository.findById(cardId)
                    .orElseThrow(() ->
                            new RuntimeException("Không tìm thấy thẻ"));

            if (!card.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Bạn không có quyền sửa thẻ này");
            }

        }
        // Insert
        else {
            card = new PaymentCard();
            card.setUser(user);
        }

        card.setCardHolder(cardHolder);

        String cleaned = cardNumber.replaceAll("\\s", "");

        card.setCardNumber(
                "**** **** **** " +
                cleaned.substring(Math.max(0, cleaned.length() - 4))
        );

        card.setCardExpiry(cardExpiry);
        card.setCardNetwork(cardNetwork);

        return cardRepository.save(card);
    }

    public PaymentBank saveBank(
            Integer bankId,
            User user,
            String bankName,
            String bankAccount,
            String bankOwner,
            String bankBranch) {

        PaymentBank bank;

        // Update
        if (bankId != null) {

            bank = bankRepository.findById(bankId)
                    .orElseThrow(() ->
                            new RuntimeException("Không tìm thấy tài khoản ngân hàng"));

            if (!bank.getUser().getId().equals(user.getId())) {
                throw new RuntimeException(
                        "Bạn không có quyền sửa tài khoản này");
            }
        }
        // Insert
        else {

            bank = new PaymentBank();
            bank.setUser(user);
        }

        bank.setBankName(bankName);
        bank.setBankAccount(bankAccount);
        bank.setBankOwner(bankOwner);
        bank.setBankBranch(bankBranch);

        return bankRepository.save(bank);
    }

    public List<PaymentCard> getCardsByUser(User user) {
        return cardRepository.findByUser(user);
    }

    public List<PaymentBank> getBanksByUser(User user) {
        return bankRepository.findByUser(user);
    }

    public void deleteCard(Integer id, User user) {
        cardRepository.findById(id).ifPresent(card -> {
            if (card.getUser().getId().equals(user.getId())) {
                cardRepository.delete(card);
            }
        });
    }

    public void deleteBank(Integer id, User user) {
        bankRepository.findById(id).ifPresent(bank -> {
            if (bank.getUser().getId().equals(user.getId())) {
                bankRepository.delete(bank);
            }
        });
    }
}