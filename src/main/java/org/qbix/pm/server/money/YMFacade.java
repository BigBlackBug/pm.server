package org.qbix.pm.server.money;

import java.math.BigDecimal;

import org.qbix.pm.server.exceptions.PMMoneyException;

import ru.yandex.money.api.YandexMoney;
import ru.yandex.money.api.YandexMoneyImpl;
import ru.yandex.money.api.response.AccountInfoResponse;
import ru.yandex.money.api.response.OperationHistoryResponse;
import ru.yandex.money.api.response.RequestPaymentResponse;
import ru.yandex.money.api.response.util.Operation;

public class YMFacade {

	private static final String CLIENT_ID = "C6EF31F132EC00811A6B1D722947314DB1FE1F8A6A2F8CB965CD086F2B3772E3";

	private static final String TOKEN = "410011397385075.13AFF95A4BCB5FD6F2E4F9D819FA1AA61FFD1F3C3D3B52CE734C8AF571F25DED725331B0226A1D72774FE6FB9EBB95E0F8223822FCDE5A60D3BB57D3AACB35D525D6BB551B6DD80BA1F0DDFE3DD9D66F19CA231A788B46246F62C5BE081A7BD59183954B323F052722EB7A8FD445A8DAB99FBD9721B3CAB0C1DBD15A7E5C9BF1";

	private YandexMoney ym = new YandexMoneyImpl(CLIENT_ID);

	public void processP2Ppayment(String toYMAcc, BigDecimal amount,
			String comment, String mess) throws PMMoneyException {
		try {
			RequestPaymentResponse rpr = ym.requestPaymentP2P(TOKEN, toYMAcc,
					amount, comment, mess);
			ym.processPaymentByWallet(TOKEN, rpr.getRequestId());
		} catch (Exception e) {
			throw new PMMoneyException(
					"Error while processing p2p transferring", e);
		}
	}

	public BigDecimal getBalance() throws PMMoneyException {
		try {
			AccountInfoResponse air = ym.accountInfo(TOKEN);
			return air.getBalance();
		} catch (Exception e) {
			throw new PMMoneyException("Error while getting curr balance", e);
		}
	}

	public String getPaymentHistoryAsString(int startRecord, int records)
			throws PMMoneyException {
		try {
			OperationHistoryResponse ohr = ym.operationHistory(TOKEN,
					startRecord, records);
			StringBuilder sb = new StringBuilder();
			for (Operation op : ohr.getOperations()) {
				sb.append(op.toString());
				sb.append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			throw new PMMoneyException("Error while getting payment history", e);
		}
	}

}
