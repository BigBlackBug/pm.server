package org.qbix.pm.server.dto;

import java.util.HashMap;
import java.util.Map;

import org.qbix.pm.server.model.VictoryCriteria;

import com.google.gson.Gson;

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

	/** parsed id */
	public Long parserId = 0L;

	public Map<String, Object> criteria = new HashMap<String, Object>();

	public void setResolverId(Long parserId) {
		this.parserId = parserId;
	}

	public Long getParserId() {
		return parserId;
	}

	public void setCriteria(Map<String, Object> criteria) {
		this.criteria = criteria;
	}
	
	public Map<String, Object> getCriteria() {
		return criteria;
	}
	
	@Override
	public VictoryCriteria convertToEntity() {
		VictoryCriteria criteria = new VictoryCriteria();
		criteria.setParserId(parserId);
		criteria.setJsonParams(new Gson().toJson(criteria));
		return criteria;
	}

}
