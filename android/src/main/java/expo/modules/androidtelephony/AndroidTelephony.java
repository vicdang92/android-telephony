package expo.modules.androidtelephony;

import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import android.content.Context;
import android.telephony.TelephonyManager;

import android.telephony.CellIdentity;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityTdscdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoTdscdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthNr;
import android.telephony.CellSignalStrengthTdscdma;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;


public class AndroidTelephony {
    public JSONObject execute(Context context, String action) throws JSONException {
        if ("getCarrierInfo".equals(action)) {
            return this.getCarrierInfo(context);
        } else if ("getAllCellInfo".equals(action)) {
            return this.getAllCellInfo(context);
        } else {
            JSONObject s = new JSONObject();
            s.put("ERROR", "Unknown action");
            return s;
        }
    }

    private JSONObject getCarrierInfo(Context context) throws JSONException {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        JSONObject r = new JSONObject();
        r.put("carrierName", tm.getNetworkOperatorName());
        r.put("isoCountryCode", tm.getNetworkCountryIso());
        r.put("mccmnc", tm.getNetworkOperator());
        return r;
    }

    public JSONObject getAllCellInfo(Context context) throws JSONException {
        JSONObject result = new JSONObject();

        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        if (telephonyManager == null) {
            result.put("ERROR", "TelephonyManager not available");
            return result;
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            result.put("ERROR", "ACCESS_FINE_LOCATION permission not granted");
            return result;
        }
        List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();

        if (cellInfoList == null || cellInfoList.isEmpty()) {
            result.put("CELL_INFO_LIST", new JSONArray());
            return result;
        }

        JSONArray cellInfoJsonArray = new JSONArray();

        for (CellInfo cellInfo : cellInfoList) {
            JSONObject cellInfoJson = new JSONObject();

            cellInfoJson.put("REGISTERED", cellInfo.isRegistered());
            cellInfoJson.put("TIMESTAMP", cellInfo.getTimeStamp());

            if (cellInfo instanceof CellInfoGsm cellInfoGsm) {

                CellIdentityGsm cellIdentityGsm = cellInfoGsm.getCellIdentity();
                CellSignalStrengthGsm cellSignalStrengthGsm = cellInfoGsm.getCellSignalStrength();

                cellInfoJson.put("TYPE", "GSM");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    cellIdentityToJson(cellIdentityGsm, cellInfoJson);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    cellSignalStrengthToJson(cellSignalStrengthGsm, cellInfoJson);
                }

            } else if (cellInfo instanceof CellInfoLte cellInfoLte) {

                CellIdentityLte cellIdentityLte = cellInfoLte.getCellIdentity();
                CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();

                cellInfoJson.put("TYPE", "LTE");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    cellIdentityToJson(cellIdentityLte, cellInfoJson);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    cellSignalStrengthToJson(cellSignalStrengthLte, cellInfoJson);
                }

            } else if (cellInfo instanceof CellInfoWcdma cellInfoWcdma) {

                CellIdentityWcdma cellIdentityWcdma = cellInfoWcdma.getCellIdentity();
                CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();

                cellInfoJson.put("TYPE", "WCDMA");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    cellIdentityToJson(cellIdentityWcdma, cellInfoJson);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    cellSignalStrengthToJson(cellSignalStrengthWcdma, cellInfoJson);
                }

            } else if (cellInfo instanceof CellInfoCdma cellInfoCdma) {

                CellIdentityCdma cellIdentityCdma = cellInfoCdma.getCellIdentity();
                CellSignalStrengthCdma cellSignalStrengthCdma = cellInfoCdma.getCellSignalStrength();

                cellInfoJson.put("TYPE", "CDMA");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    cellIdentityToJson(cellIdentityCdma, cellInfoJson);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    cellSignalStrengthToJson(cellSignalStrengthCdma, cellInfoJson);
                }

            } else if (cellInfo instanceof CellInfoNr cellInfoNr) {

                CellIdentityNr cellIdentityNr = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    cellIdentityNr = (CellIdentityNr) cellInfoNr.getCellIdentity();
                }
                CellSignalStrengthNr cellSignalStrengthNr = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    cellSignalStrengthNr = (CellSignalStrengthNr) cellInfoNr
                            .getCellSignalStrength();
                }

                cellInfoJson.put("TYPE", "NR");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    cellIdentityToJson(cellIdentityNr, cellInfoJson);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    cellSignalStrengthToJson(cellSignalStrengthNr, cellInfoJson);
                }

            } else if (cellInfo instanceof CellInfoTdscdma cellInfoTdscdma) {

                CellIdentityTdscdma cellIdentityTdscdma = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    cellIdentityTdscdma = cellInfoTdscdma.getCellIdentity();
                }
                CellSignalStrengthTdscdma cellSignalStrengthTdscdma = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    cellSignalStrengthTdscdma = cellInfoTdscdma.getCellSignalStrength();
                }

                cellInfoJson.put("TYPE", "TDSCDMA");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        cellIdentityToJson(cellIdentityTdscdma, cellInfoJson);
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    cellSignalStrengthToJson(cellSignalStrengthTdscdma, cellInfoJson);
                }

            } else {
                cellInfoJson.put("TYPE", "Unknown");
            }

            cellInfoJsonArray.put(cellInfoJson);
        }

        result.put("CELL_INFO_LIST", cellInfoJsonArray);
        return result;
    }

    private static void cellIdentityToJson(CellIdentity cellIdentity, JSONObject cellinfo) throws JSONException {
        if (cellIdentity instanceof CellIdentityGsm cellIdentityGsm) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_MCC", cellIdentityGsm.getMccString());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_MNC", cellIdentityGsm.getMncString());
            }
            cellinfo.put("CELL_IDENTITY_LAC", cellIdentityGsm.getLac());
            cellinfo.put("CELL_IDENTITY_CID", cellIdentityGsm.getCid());
            cellinfo.put("CELL_IDENTITY_ARFCN", cellIdentityGsm.getArfcn());
            cellinfo.put("CELL_IDENTITY_BSIC", cellIdentityGsm.getBsic());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_OPERATOR_ALPHA_LONG", cellIdentityGsm.getOperatorAlphaLong());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_OPERATOR_ALPHA_SHORT", cellIdentityGsm.getOperatorAlphaShort());
            }

        } else if (cellIdentity instanceof CellIdentityLte cellIdentityLte) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_MCC", cellIdentityLte.getMccString());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_MNC", cellIdentityLte.getMncString());
            }
            cellinfo.put("CELL_IDENTITY_CI", cellIdentityLte.getCi());
            cellinfo.put("CELL_IDENTITY_PCI", cellIdentityLte.getPci());
            cellinfo.put("CELL_IDENTITY_TAC", cellIdentityLte.getTac());
            cellinfo.put("CELL_IDENTITY_EARFCN", cellIdentityLte.getEarfcn());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_BANDWIDTH", cellIdentityLte.getBandwidth());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_OPERATOR_ALPHA_LONG", cellIdentityLte.getOperatorAlphaLong());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_OPERATOR_ALPHA_SHORT", cellIdentityLte.getOperatorAlphaShort());
            }
        } else if (cellIdentity instanceof CellIdentityWcdma cellIdentityWcdma) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_MCC", cellIdentityWcdma.getMccString());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_MNC", cellIdentityWcdma.getMncString());
            }
            cellinfo.put("CELL_IDENTITY_LAC", cellIdentityWcdma.getLac());
            cellinfo.put("CELL_IDENTITY_CID", cellIdentityWcdma.getCid());
            cellinfo.put("CELL_IDENTITY_PSC", cellIdentityWcdma.getPsc());
            cellinfo.put("CELL_IDENTITY_UARFCN", cellIdentityWcdma.getUarfcn());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_OPERATOR_ALPHA_LONG", cellIdentityWcdma.getOperatorAlphaLong());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_OPERATOR_ALPHA_SHORT", cellIdentityWcdma.getOperatorAlphaShort());
            }
        } else if (cellIdentity instanceof CellIdentityCdma cellIdentityCdma) {
            cellinfo.put("CELL_IDENTITY_NETWORK_ID", cellIdentityCdma.getNetworkId());
            cellinfo.put("CELL_IDENTITY_SYSTEM_ID", cellIdentityCdma.getSystemId());
            cellinfo.put("CELL_IDENTITY_BASE_STATION_ID", cellIdentityCdma.getBasestationId());
            cellinfo.put("CELL_IDENTITY_LONGITUDE", cellIdentityCdma.getLongitude());
            cellinfo.put("CELL_IDENTITY_LATITUDE", cellIdentityCdma.getLatitude());
        } else if (cellIdentity instanceof CellIdentityNr cellIdentityNr) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cellinfo.put("CELL_IDENTITY_MCC", cellIdentityNr.getMccString());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cellinfo.put("CELL_IDENTITY_MNC", cellIdentityNr.getMncString());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cellinfo.put("CELL_IDENTITY_NCI", cellIdentityNr.getNci());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cellinfo.put("CELL_IDENTITY_PCI", cellIdentityNr.getPci());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cellinfo.put("CELL_IDENTITY_TAC", cellIdentityNr.getTac());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cellinfo.put("CELL_IDENTITY_NRARFCN", cellIdentityNr.getNrarfcn());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                cellinfo.put("CELL_IDENTITY_BANDS", Arrays.toString(cellIdentityNr.getBands()));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_OPERATOR_ALPHA_LONG", cellIdentityNr.getOperatorAlphaLong());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_OPERATOR_ALPHA_SHORT", cellIdentityNr.getOperatorAlphaShort());
            }
        } else if (cellIdentity instanceof CellIdentityTdscdma cellIdentityTdscdma) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_MCC", cellIdentityTdscdma.getMccString());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_MNC", cellIdentityTdscdma.getMncString());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_LAC", cellIdentityTdscdma.getLac());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_CID", cellIdentityTdscdma.getCid());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_CPID", cellIdentityTdscdma.getCpid());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cellinfo.put("CELL_IDENTITY_UARFCN", cellIdentityTdscdma.getUarfcn());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_OPERATOR_ALPHA_LONG", cellIdentityTdscdma.getOperatorAlphaLong());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cellinfo.put("CELL_IDENTITY_OPERATOR_ALPHA_SHORT", cellIdentityTdscdma.getOperatorAlphaShort());
            }

        } else {
            cellinfo.put("CELL_IDENTITY_ERROR", "Unknown CellIdentity type");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static void cellSignalStrengthToJson(CellSignalStrength signalStrength, JSONObject cellinfo)
            throws JSONException {

        if (signalStrength instanceof CellSignalStrengthGsm signalStrengthGsm) {
            cellinfo.put("CELL_SIGNAL_STRENGTH_DBM", signalStrengthGsm.getDbm());
            cellinfo.put("CELL_SIGNAL_STRENGTH_LEVEL", signalStrengthGsm.getLevel());
            cellinfo.put("CELL_SIGNAL_STRENGTH_ASU_LEVEL", signalStrengthGsm.getAsuLevel());
            cellinfo.put("CELL_SIGNAL_STRENGTH_TIMING_ADVANCE", signalStrengthGsm.getTimingAdvance());

        } else if (signalStrength instanceof CellSignalStrengthLte signalStrengthLte) {
            cellinfo.put("CELL_SIGNAL_STRENGTH_DBM", signalStrengthLte.getDbm());
            cellinfo.put("CELL_SIGNAL_STRENGTH_LEVEL", signalStrengthLte.getLevel());
            cellinfo.put("CELL_SIGNAL_STRENGTH_ASU_LEVEL", signalStrengthLte.getAsuLevel());
            cellinfo.put("CELL_SIGNAL_STRENGTH_RSRP", signalStrengthLte.getRsrp());
            cellinfo.put("CELL_SIGNAL_STRENGTH_RSRQ", signalStrengthLte.getRsrq());
            cellinfo.put("CELL_SIGNAL_STRENGTH_RSSNR", signalStrengthLte.getRssnr());
            cellinfo.put("CELL_SIGNAL_STRENGTH_CQI", signalStrengthLte.getCqi());
            cellinfo.put("CELL_SIGNAL_STRENGTH_TIMING_ADVANCE", signalStrengthLte.getTimingAdvance());

        } else if (signalStrength instanceof CellSignalStrengthWcdma signalStrengthWcdma) {
            cellinfo.put("CELL_SIGNAL_STRENGTH_DBM", signalStrengthWcdma.getDbm());
            cellinfo.put("CELL_SIGNAL_STRENGTH_LEVEL", signalStrengthWcdma.getLevel());
            cellinfo.put("CELL_SIGNAL_STRENGTH_ASU_LEVEL", signalStrengthWcdma.getAsuLevel());

        } else if (signalStrength instanceof CellSignalStrengthCdma signalStrengthCdma) {
            cellinfo.put("CELL_SIGNAL_STRENGTH_DBM", signalStrengthCdma.getDbm());
            cellinfo.put("CELL_SIGNAL_STRENGTH_LEVEL", signalStrengthCdma.getLevel());
            cellinfo.put("CELL_SIGNAL_STRENGTH_ASU_LEVEL", signalStrengthCdma.getAsuLevel());
            cellinfo.put("CELL_SIGNAL_STRENGTH_CDMA_DBM", signalStrengthCdma.getCdmaDbm());
            cellinfo.put("CELL_SIGNAL_STRENGTH_CDMA_ECIO", signalStrengthCdma.getCdmaEcio());
            cellinfo.put("CELL_SIGNAL_STRENGTH_EVDO_DBM", signalStrengthCdma.getEvdoDbm());
            cellinfo.put("CELL_SIGNAL_STRENGTH_EVDO_ECIO", signalStrengthCdma.getEvdoEcio());
            cellinfo.put("CELL_SIGNAL_STRENGTH_EVDO_SNR", signalStrengthCdma.getEvdoSnr());
        } else if (signalStrength instanceof CellSignalStrengthNr signalStrengthNr) {
            cellinfo.put("CELL_SIGNAL_STRENGTH_DBM", signalStrengthNr.getDbm());
            cellinfo.put("CELL_SIGNAL_STRENGTH_LEVEL", signalStrengthNr.getLevel());
            cellinfo.put("CELL_SIGNAL_STRENGTH_SS_RSRP", signalStrengthNr.getSsRsrp());
            cellinfo.put("CELL_SIGNAL_STRENGTH_SS_RSRQ", signalStrengthNr.getSsRsrq());
            cellinfo.put("CELL_SIGNAL_STRENGTH_SS_SINR", signalStrengthNr.getSsSinr());
            cellinfo.put("CELL_SIGNAL_STRENGTH_CSI_RSRP", signalStrengthNr.getCsiRsrp());
            cellinfo.put("CELL_SIGNAL_STRENGTH_CSI_RSRQ", signalStrengthNr.getCsiRsrq());
            cellinfo.put("CELL_SIGNAL_STRENGTH_CSI_SINR", signalStrengthNr.getCsiSinr());

        } else if (signalStrength instanceof CellSignalStrengthTdscdma signalStrengthTdscdma) {
            cellinfo.put("CELL_SIGNAL_STRENGTH_DBM", signalStrengthTdscdma.getDbm());
            cellinfo.put("CELL_SIGNAL_STRENGTH_LEVEL", signalStrengthTdscdma.getLevel());
            cellinfo.put("CELL_SIGNAL_STRENGTH_ASU_LEVEL", signalStrengthTdscdma.getAsuLevel());
            cellinfo.put("CELL_SIGNAL_STRENGTH_RSCP", signalStrengthTdscdma.getRscp());

        } else {
            cellinfo.put("CELL_SIGNAL_STRENGTH_ERROR", "Unknown CellSignalStrength type");
        }
    }
}