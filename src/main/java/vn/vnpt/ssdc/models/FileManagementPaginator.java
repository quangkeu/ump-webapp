package vn.vnpt.ssdc.models;

import vn.vnpt.ssdc.api.client.AcsApiClient;
import vn.vnpt.ssdc.api.client.FileManagementClient;
import vn.vnpt.ssdc.api.model.AcsResponse;
import vn.vnpt.ssdc.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lamborgini on 3/6/2017.
 */
public class FileManagementPaginator extends Paginator<FileManagement> {
    public List<FileManagement> fileManagements;
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final String PAGE_SIZE = "limit";
    public static final String PAGE_QUERY = "query";
    public static final String PAGE_OFFSET = "offset";
    public static final String PAGE_PARAM = "parameters";
    public static final String PAGE_CURRENT = "indexPage";
    public static final String PAGE_MANUFACTURER = "Manufacturer";
    public static final String PAGE_MODEL_NAME = "ModelName";
    public static final String PAGE_FILE_TYPE = "FileType";
    public Map<String, String> acsQuery = new HashMap<>();
    public int lastPage;
    public FileManagementClient fileManagementClient;
    public Map<String, String> indexParams = new HashMap<>();
    public int currentPage;
    public int pageSize = DEFAULT_PAGE_SIZE;
    public String searchManufacturerDataOld = "";
    public String searchModelNameDataOld = "";
    public String searcFileTypeDataOld = "";

    public FileManagementPaginator() {
        this.currentPage = 0;
    }

    public void loadResult(Map<String, String> requestParams) {
        // call webservice of backend
        AcsResponse response = fileManagementClient.findFileManagement(acsQuery);
        this.totalPages = response.nbOfItems;
        this.fileManagements = FileManagement.fromJsonString(response.body, indexParams.keySet());
        if (this.totalPages == 0) {
            this.lastPage = 1;
        } else {
            int page = this.totalPages % (requestParams.size() == 0 ? DEFAULT_PAGE_SIZE
                    : Integer.valueOf(requestParams.get(PAGE_SIZE)));
            int pageTotal = this.totalPages / (requestParams.size() == 0 ? DEFAULT_PAGE_SIZE
                    : Integer.valueOf(requestParams.get(PAGE_SIZE)));
            this.lastPage = page == 0 ? pageTotal : pageTotal + 1;
        }

    }
    public static boolean isExits(HashMap<String, String> requestParams, String key) {
        if (requestParams.containsKey(key) && requestParams.get(key) != null) {
            return true;
        }
        return false;
    }
    public void parseParam(HashMap<String, String> requestParams) {
        String queryInput = null;
        this.number++;
        if (requestParams != null && !requestParams.isEmpty()) {

            if (isExits(requestParams, PAGE_CURRENT)) {
                this.currentPage = Integer.parseInt(requestParams.get(PAGE_CURRENT));
            }
            if (isExits(requestParams, PAGE_SIZE) && !("0").equals(requestParams.get(PAGE_SIZE))) {
                this.pageSize = Integer.parseInt(requestParams.get(PAGE_SIZE));
            }
            if (isExits(requestParams, PAGE_QUERY)) {
                queryInput = requestParams.get(PAGE_QUERY);
            }
        }
        this.acsQuery.put(PAGE_PARAM, String.join(",", this.indexParams.keySet()));
        this.acsQuery.put(PAGE_SIZE, String.valueOf(this.pageSize));

        if (queryInput != null) {
            this.acsQuery.put(PAGE_QUERY, queryInput);
        }
        int offset = this.currentPage * this.pageSize;
        this.acsQuery.put(PAGE_OFFSET, String.valueOf(offset));
        this.acsQuery.put(PAGE_CURRENT, String.valueOf(currentPage - 1));
        if (requestParams.containsKey("group")) {
            this.acsQuery.put("query", String.format("{\"_groups\":\"%s\"}", requestParams.get("group")));
        } else {
            this.acsQuery.put("query", "{}");
        }

    }

    public void parseParamSearch(HashMap<String, String> requestParams, Map<String, String> fileManagementIndexParams,
                                 Map<String, String> stringStringMap) {
        // offset value
        this.currentPage = Integer.parseInt(requestParams.get(PAGE_CURRENT));
        this.number = Integer.parseInt(requestParams.get(PAGE_CURRENT));
        this.acsQuery.put(PAGE_SIZE, requestParams.get(PAGE_SIZE));
        int offset = (this.currentPage - 1) * this.pageSize;
        this.acsQuery.put(PAGE_OFFSET, String.valueOf(offset));
        this.acsQuery.put(PAGE_PARAM, String.join(",", fileManagementIndexParams.keySet()));
        this.acsQuery.put(PAGE_QUERY, StringUtils.queryFileManagement(requestParams, stringStringMap));

    }
}
