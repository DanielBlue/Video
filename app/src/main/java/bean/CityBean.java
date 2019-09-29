package bean;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Created by JC on 2019/9/6.
 */

public class CityBean implements IPickerViewData {

    /**
     * province : 北京
     * city_list : ["北京"]
     */

    private String province;
    private List<String> city_list;

    @Override
    public String getPickerViewText() {
        return this.province;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<String> getCity_list() {
        return city_list;
    }

    public void setCity_list(List<String> city_list) {
        this.city_list = city_list;
    }
}
