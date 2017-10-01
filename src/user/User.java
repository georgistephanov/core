package user;

import lib.payment.VisaCard;

public interface User {
	long account_num = 100_123_00;
	long card_num = 236_1218_00;
	boolean status = false;
	boolean logged_in = false;

	VisaCard card = new VisaCard();
}
