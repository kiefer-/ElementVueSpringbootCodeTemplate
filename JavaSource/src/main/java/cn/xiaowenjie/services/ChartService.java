package cn.xiaowenjie.services;

import cn.xiaowenjie.beans.UploadRecord;
import cn.xiaowenjie.chartbeans.Entry;
import cn.xiaowenjie.chartbeans.LineBean;
import cn.xiaowenjie.common.beans.PageResp;
import cn.xiaowenjie.daos.EndDataDao;
import cn.xiaowenjie.daos.UploadRecordDao;
import cn.xiaowenjie.tool.ConfigUtil;
import cn.xiaowenjie.tool.EndData;
import cn.xiaowenjie.tool.ReadLog2CSV;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static cn.xiaowenjie.common.utils.CheckUtil.check;

/**
 *  生成图表
 */
@Service
@Slf4j
public class ChartService {


    @Autowired
    EndDataDao endDataDao;


    public LineBean line(long uploadRecordId) {
        LineBean bean = new LineBean();

        //
        bean.setColumns(Lists.newArrayList("日期", "盈亏"));

        // 得到记录
        List<EndData> endDatas =  endDataDao.findAllByRecordId(uploadRecordId);

        log.info("uploadRecordId: " + uploadRecordId +", data size: " + endDatas.size() );

        // 转换为图表对象
        List<Entry> rows = endDatas.stream().map(ChartService::toChartEntry).collect(Collectors.toList());

        bean.setRows(rows);

        return bean;
    }

    private static Entry toChartEntry(EndData data) {
        Entry e = new Entry();

        e.addX("日期", data.getDate());
        e.addY("盈亏", data.getVolume());

        return e;
    }
}
