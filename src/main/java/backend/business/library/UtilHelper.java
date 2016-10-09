package backend.business.library;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

public class UtilHelper
{
    public static <FromBean, ToBean> List<ToBean> mapListOfEnitiesToDTOs(Mapper mapper, List<FromBean> beans, Class<ToBean> clazz)
    {
        List<ToBean> list = new ArrayList<ToBean>();
        for (FromBean bean : beans)
        {
            if (bean != null)
            {
                list.add(mapper.map(bean, clazz));
            }
        }
        return list;
    }
}
