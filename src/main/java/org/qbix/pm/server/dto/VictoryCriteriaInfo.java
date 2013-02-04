package org.qbix.pm.server.dto;

import java.util.HashMap;
import java.util.Map;

import org.qbix.pm.server.model.VictoryCriteria;

//json
/**"victory_criteria" : {
	"parser_id" : , 2 // 
	"criteria" : {
		team1:{
			winner : "legion",
			time : {
				"$eq" : 15,
				"threshold" : 3
			},
			players : [
				player : {
					nick : "bigblackbug",
					kills : {"$gt" : 15}
				}
			]
		},
		team2:{
			winner : "hellbourne",
		}
	}
}**/
public class VictoryCriteriaInfo extends
		AbstractInfo<VictoryCriteria> {

	private static final long serialVersionUID = 2549283300751914989L;

	/** resolver id */
	public Long parserId = 0L;

	public Map<String, String> params = new HashMap<String, String>();

	public void setResolverId(Long parserId) {
		this.parserId = parserId;
	}

	public Long getParserId() {
		return parserId;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public Map<String, String> getParams() {
		return params;
	}

	@Override
	public VictoryCriteria convertToEntity() {
		VictoryCriteria criteria = new VictoryCriteria();
		criteria.setParserId(parserId);
		criteria.setParameters(params);
		return criteria;
	}

}
