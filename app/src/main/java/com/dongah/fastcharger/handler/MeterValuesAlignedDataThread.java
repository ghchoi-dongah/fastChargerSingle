package com.dongah.fastcharger.handler;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.dongah.fastcharger.MainActivity;
import com.dongah.fastcharger.basefunction.ChargingCurrentData;
import com.dongah.fastcharger.utils.LogDataSave;
import com.dongah.fastcharger.websocket.ocpp.core.MeterValue;
import com.dongah.fastcharger.websocket.ocpp.core.MeterValuesRequest;
import com.dongah.fastcharger.websocket.ocpp.core.SampledValue;
import com.dongah.fastcharger.websocket.ocpp.utilities.ZonedDateTimeConvert;
import com.dongah.fastcharger.websocket.socket.SocketReceiveMessage;
import com.dongah.fastcharger.websocket.socket.SocketState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class MeterValuesAlignedDataThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(MeterValuesAlignedDataThread.class);

    boolean stopped = false;
    int delayTime;
    int connectorId;

    Object payload, call;
    final String CALL_FORMAT = "[2, \"%s\", \"%s\", %s]";

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public int getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(int connectorId) {
        this.connectorId = connectorId;
    }

    public MeterValuesAlignedDataThread(int connectorId, int delayTime) {
        super();
        this.connectorId = connectorId;
        this.delayTime = delayTime;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {
        super.run();
        int count = 0;
        while (!isStopped()) {
            try {
                sleep(1000);
                count++;
                if (count >= (getDelayTime())) {
                    count = 0;
                    SocketReceiveMessage socketReceiveMessage = ((MainActivity) MainActivity.mContext).getSocketReceiveMessage();
                    ZonedDateTimeConvert zonedDateTimeConvert = new ZonedDateTimeConvert();
                    ZonedDateTime timestamp = zonedDateTimeConvert.doZonedDateTimeToDatetime();
                    ChargingCurrentData chargingCurrentData = ((MainActivity) MainActivity.mContext).getChargingCurrentData();
                    SampledValue[] sampledValues = chargingCurrentData.getSampleValueData().getSampledValues(chargingCurrentData);
                    MeterValue[] meterValues = {new MeterValue(timestamp, sampledValues)};
                    MeterValuesRequest meterValuesRequest = new MeterValuesRequest(chargingCurrentData.getConnectorId());
                    meterValuesRequest.setMeterValue(meterValues);
                    meterValuesRequest.setTransactionId(chargingCurrentData.getTransactionId());
                    if (Objects.equals(socketReceiveMessage.getSocket().getState(), SocketState.OPEN)) {
                        socketReceiveMessage.onSend(getConnectorId(), meterValuesRequest.getActionName(), meterValuesRequest);
                    } else {
                        String uuid = UUID.randomUUID().toString();
                        payload = packPayload(meterValuesRequest);
                        call = String.format(CALL_FORMAT, uuid, meterValuesRequest.getActionName(), payload);
                        LogDataSave logDataSave = new LogDataSave();
                        logDataSave.makeDump(call.toString());
                    }
                }
            } catch (Exception e) {
                logger.error(" thread error : {}", e.getMessage());
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public Object packPayload(Object payload) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeSerializer());
        Gson gson = builder.create();
        return gson.toJson(payload);
    }


    private static class ZonedDateTimeSerializer implements JsonSerializer<ZonedDateTime> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public JsonElement serialize(ZonedDateTime zonedDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(zonedDateTime.format(DateTimeFormatter.ISO_INSTANT));
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        setStopped(true);
    }
}
