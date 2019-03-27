package com.yihexinda.platformweb.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yihexinda.auth.constant.AuthoritiesConstants;
import com.yihexinda.auth.dto.SysUserDto;
import com.yihexinda.auth.dto.SysUserQueryDto;
import com.yihexinda.core.dto.Json;
import com.yihexinda.core.utils.ExcelXUtils;
import com.yihexinda.data.dto.OrderDetailsDto;
import com.yihexinda.data.dto.OrderDto;
import com.yihexinda.data.dto.UserDto;
import com.yihexinda.data.enums.OperateTypeEnum;
import com.yihexinda.platformweb.client.AuthClient;
import com.yihexinda.platformweb.client.DataOrderClient;
import com.yihexinda.platformweb.client.DataUserClient;
import com.yihexinda.platformweb.security.SecurityHelper;
import com.yihexinda.platformweb.security.SecurityUtils;
import com.yihexinda.platformweb.service.AuthService;
import com.yihexinda.platformweb.service.OperateLogService;
import com.yihexinda.platformweb.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Jack
 * @date 2018/10/12.
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {
    @Autowired
    private SecurityHelper securityHelper;
    @Autowired
    private DataOrderClient orderClient;
    @Autowired
    private AuthClient authClient;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @Autowired
    private OperateLogService operateLogService;
    @Autowired
    private DataUserClient userClient;

    @RequestMapping("/test")
    public String test(Model model) {
        model.addAttribute("name", "test-name");
        return "test";
    }

    @RequestMapping("/testUser")
    @ResponseBody
    public Object testUser(Model model) {
        return securityHelper.getCurrentUser();
    }

    @RequestMapping("/testUsername")
    @ResponseBody
    public Object testUsername(Model model) {
        return SecurityUtils.getCurrentUserLogin();
    }

    @RequestMapping("/testLogin")
    @ResponseBody
    public Object testLogin(HttpServletRequest request) {
        String username = request.getParameter("username");
        if (StringUtils.isEmpty(username)) username = "platform-user";
        securityHelper.login(username, request);
        SysUserDto userDto = authService.findSysUserDtoByUsername(username);
        userService.createOrUpdateUser(userDto);
        return SecurityUtils.getCurrentUserLogin();
    }

    @RequestMapping("/testSessionId")
    @ResponseBody
    public Object testToken(HttpServletRequest request) {
        return request.getSession().getId();
    }

    @RequestMapping("/clearCookie")
    @ResponseBody
    public Object testToken(HttpServletResponse response) {
        Cookie cookie = new Cookie("SESSION", null); // Not necessary, but saves bandwidth.
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // Don't set to -1 or it will become a session cookie!
        response.addCookie(cookie);
        return "clear";
    }

//    @RequestMapping("/testOrderPage")
//    @ResponseBody
//    public  Json<Page<FindOrderReturnDto>> testOrderPage() {
//        FindOrderPageDto findOrderPageDto = new FindOrderPageDto();
//        findOrderPageDto.setStart(0);
//        findOrderPageDto.setLimit(10);
//        findOrderPageDto.setStartDate("2018-10-10");
//        findOrderPageDto.setEndDate("2018-10-20");
//        Json<Page<FindOrderReturnDto>> orderPage = orderClient.findOrderPage(findOrderPageDto);
//        return orderPage;
//    }


    @RequestMapping("/testAuthClient")
    @ResponseBody
    public SysUserDto testAuthClient() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "test123");
        return authClient.findUserByUsernameForm(map);
    }

    @RequestMapping("/testAuthClient1")
    @ResponseBody
    public SysUserDto testAuthClient1() {
        String loginUsername = "test123";
        SysUserQueryDto findSysUserDto = new SysUserQueryDto();
        findSysUserDto.setUsername(loginUsername);
        SysUserDto userByUsername = authClient.findUserByUsername(findSysUserDto);
        return userByUsername;
    }

    @RequestMapping("/testTimeout")
    @ResponseBody
    public String testTimeout() throws InterruptedException {
        Thread.sleep(10000);
        return "success";
    }

    @RequestMapping("/testEnumJackson")
    @ResponseBody
    public String testEnumJackson() throws InterruptedException, IOException {
        String json
                = "{\"id\":\"123\",\"orderSource\":\"营业部订单\",\"orderType\":\"出票\"}";
        ObjectMapper mapper = new ObjectMapper();
        OrderDto bean = mapper
                .readerFor(OrderDto.class)
                .readValue(json);
        System.out.println(bean.getOrderSource());
        System.out.println(bean.getOrderType());
        String str = mapper.writeValueAsString(bean);
        System.out.println(str);
        return "success";
    }

    /**
     * 手动派单接口
     *
     * @return
     */
    @RequestMapping("/testDistributeOrder")
    @ResponseBody
    public String distributeOrder() {
        OrderDto orderDto = new OrderDto();
        //员工工号
        orderDto.setOutTicketMember("bolin");
        //订单id
        orderDto.setId(" 269054c9e1af4fd9a26649cd702a23f9");
        Json json = orderClient.distributeOrder("269054c9e1af4fd9a26649cd702a23f9", "bolin");
        return "ok";
    }

    @RequestMapping("/testAuthorities")
    @ResponseBody
    public Object testAuthorities() throws InterruptedException {
//        return userService.findAuthoritiesByUserId(1002);
//        return SecurityUtils.hasAuthority(AuthoritiesConstants.NODE_GRAB_ORDER_TYPE_ALL);
        return SecurityUtils.getAuthorities();
    }


    /**
     * 处理派单--派单确认
     *
     * @return
     */
    @RequestMapping("/testcomfirmDispatchOrder")
    @ResponseBody
    public String comfirmDispatch() {
        Json json = orderClient.comfirmDispatchOrder(" 269054c9e1af4fd9a26649cd702a23f9", 20L);
        return "ok";
    }

    @RequestMapping("/testLog")
    @ResponseBody
    public Object testLog() {
//        return userService.findAuthoritiesByUserId(1002);
//        return SecurityUtils.hasAuthority(AuthoritiesConstants.NODE_GRAB_ORDER_TYPE_ALL);
        operateLogService.addOperateLog(securityHelper.getCurrentUser().getId(), "Order", "790bfe5e-d93d-542a-e053-3f830d0acf7b", OperateTypeEnum.抢单, "抢单成功");
        return "success";
    }

    @RequestMapping("/testFindOrderDetails")
    @ResponseBody
    public Json<OrderDetailsDto> testFindOrderDetails() {
        return orderClient.findOrderDetails("TK17122003024");
    }

    @RequestMapping("/testSyncUsers")
    @ResponseBody
    public Object testSyncUsers() {
        String usernames = "G00383,G02138,G04065,G03189,8847,8917,9393,XC,G04238,G05175,G04709,G04837,G01228,G03673,G03055,G03191,G03147,G04614,G04443,G04848,G04695,G05533,ZYFY,G8501,SHGM8457,G00954,G04097,G02972,G04310,G05222,G03357,G03693,G04117,G00997,G04140,G05315,G00093,G05177,G05479,G04632,G05297,G01303,G01446,G04348,G00359,G03445,G2641,G01024,G04178,G01866,SZHN035,G02335,G02588,G02093,G03392,G04332,G04540,G04949,G04541,G04859,G04671,G05326,8258,8529,TC,G03371,G05462,G04996,G03386,8850,9494,G05304,QN,G01249,G04817,G02264,G02778,user1,user2,G02850,G01625,G04708,G05330,G03921,G04220,G04413,G04717,G05289,8226,8827,8268GLADMIN,G05203,G03663,G04950,G04024,G00873,G02375,G04499,G01092,G01365,G03955,G04333,G02871,G04395,G05049,G04240,G05193,G00847,G00196,G00203,G04200,G03195,G02141,G03750,G02480,G05202,G01257,G04636,G04763,9696,G04875,G01998,G04873,CESHI0001,G02533,admin,1,WHYFADMIN,WHYFEBANKUSER,VETECHEBANK,VETECHADMIN,WHYFEBANK,8245,8766,8077,FQCGR,G05185,G04572,G04360,8000YI,G01836,G05350,G05349,G04961,G05231,G05156,G00856,G00597,G05023,G05345,G01781,G04831,G05027,G04467,G05281,G05168,G05443,G03863,G05229,G04258,G05359,G05400,JTGJ,G03309,G01971,SHGMEBANK,SHGMADMIN,TEST01,G00187,G01785,G01777,G04419,G00547,888002,517NA,FQCJL,G00281,G03264,H00020,G02527,G01587,G04892,G05025,G05183,G05157,G02104,G03308,G00252,G01483,G02733,G05463,G02936,G04396,G02190,G04619,TEST02,G02593,G00689,G00946,G04252,G04793,G00133,G04944,G04890,G00492,G00972,FQCZR,G02045,G04960,G03085,G03738,G02536,G01375,G04490,G00268,G02951,PTGLADMIN,G00089,G03250,G04368,T00590,G05230,G01744,G02672,G04943,WCPDWGLADMIN,VEADMIN,G04987,G05180,G00577,G00879,G04991,G05347,G00861,G04990,G05146,G05182,G04432,G05323,G05140,G05160,G05155,S00934,G02038,G02405,G04979,G03000,G04227,G04507,G04973,G04293,G04978,G05186,G04083,G05399,G05398,G05370,G02019,G03079,G03368,G02108,G04296,G05151,G05194,G03429,G03210,G05314,G03328,G05437,G02162,G01624";
        String[] usernameArr = usernames.split(",");
        for (String username : usernameArr) {
            SysUserDto sysUserDto = authService.findSysUserDtoByUsername(username);
            userService.createOrUpdateUser(sysUserDto);
            System.out.println(username);
        }
        return "success";
    }

    @RequestMapping("/testSyncAuthorities")
    @ResponseBody
    public Object testSyncAuthorities() {
        Set<String> authorities = new HashSet<>();
        authorities.add(AuthoritiesConstants.NODE_GRAB_ORDER_TYPE_CHUPIAO);
        authorities.add(AuthoritiesConstants.NODE_GRAB_ORDER_TYPE_GAIQI);
        authorities.add(AuthoritiesConstants.NODE_GRAB_ORDER_TYPE_HANGEYAN);
        authorities.add(AuthoritiesConstants.NODE_GRAB_ORDER_TYPE_TUIPIAO);
        authorities = authorities.stream().map(x -> x.replace("AUTHORITY_", "")).collect(Collectors.toSet());
        String userIds = "1517,1514,1515,1516,1518,1565,1566,1519,1520,1521,1522,1523,1524,1525,1526,1527,1528,1529,1530,1531,1532,1533,1534,1535,1536,1537,1538,1539,1540,1541,1542,1543,1544,1545,1546,1547,1548,1549,1550,1551,1552,1553,1554,1555,1556,1557,1558,1559,1560,1561,1562,1563,1564,999,1297,1298,1299,1300,1301,1302,1303,1304,1305,1306,1307,1308,1309,1310,1311,1312,1313,1314,1315,1316,1317,1318,1319,1320,1321,1322,1323,1324,1325,1326,1329,1327,1328,1330,1331,1332,1333,1334,1335,1336,1337,1338,1339,1340,1341,1342,1343,1344,1345,1346,1347,1348,1349,1350,1351,1352,1353,1354,1355,1356,1357,1358,1359,1360,1361,1362,1363,1364,1365,1366,1367,1368,1369,1370,1371,1372,1373,1374,1375,1376,1377,1378,1379,1380,1381,1382,1383,1384,1385,1386,1387,1388,1389,1390,1391,1392,1393,1394,1395,1396,1397,1398,1399,1400,1401,1402,1403,1404,1405,1406,1407,1408,1409,1410,1411,1414,1412,1413,1415,1416,1417,1418,1419,1420,1421,1422,1423,1424,1425,1426,1427,1428,1429,1430,1431,1432,1433,1434,1435,1436,1457,1458,1459,1460,1461,1462,1463,1437,1438,1439,1440,1441,1442,1443,1444,1445,1446,1447,1448,1449,1450,1451,1452,1453,1454,1455,1456,1464,1465,1466,1467,1468,1469,1470,1471,1472,1473,1474,1475,1476,1477,1478,1479,1480,1481,1482,1483,1484,1485,1486,1487,1488,1489,1490,1491,1492,1493,1513,1494,1495,1496,1497,1498,1499,1500,1501,1502,1503,1504,1505,1506,1507,1508,1509,1510,1511,1512";
        String[] userIdArr = userIds.split(",");
        for (String userId : userIdArr) {
            userClient.updateAuthorities(Integer.valueOf(userId), authorities);
        }
        return null;
    }

    @RequestMapping("/testExportExcel")
    @ResponseBody
    public void testExportExcel(HttpServletResponse response, HttpServletRequest request) {
        try {
            String title = "测试列表";
            String[] headers = new String[]{"用户ID", "工号", "姓名", "公司", "部门"};
            List<List<String>> dataList = new ArrayList<>();
            Json<List<UserDto>> json = userService.findAvailableUsersToAssign("79e2c959-8823-7289-e053-3f830d0a0fbe");
            json.getObj().forEach(user -> {
                dataList.add(Arrays.asList(user.getUserId().toString(), user.getUsername(), user.getName(), user.getCompName(), user.getDeptName()));
            });
            SXSSFWorkbook workbook = ExcelXUtils.getInstance().exportExcelXWithCommonData(title, headers, null, dataList, false);

            List<List<String>> appendDataList = new ArrayList<>();
            appendDataList.add(Arrays.asList("1002", "node-user", "网点用户", "义合信达", "技术部"));
            ExcelXUtils.getInstance().appendRows(workbook, title, headers.length, appendDataList);

            response.reset();

            //火狐浏览器乱码解决
            ExcelXUtils.getInstance().fireFoxEnCode(request, response, title, workbook);

        } catch (Exception e) {
            log.error("testExportExcel error {}", e);
        }
    }

}
