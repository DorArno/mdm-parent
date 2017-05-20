package com.mdm.mq.producter;

import com.mdm.core.jms.MessageObserver;
import com.mdm.core.jms.ObserverRepository;
import com.mdm.core.jms.bean.Schema;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by HUYU006 on 2017-01-04.
 */
@Repository
public class ObserverRepositoryImpl implements ObserverRepository {
    @Override
    public List<MessageObserver> getObservers(Schema schema, List<String> besides) {
        return null;
    }

    @Override
    public MessageObserver getObserver(Schema schema) {
        return null;
    }

}
