package com.humbhi.blackbox.ui.data.network.api

object ApiEndpoints {

    const val BASE_URL = ""

    const val LOGIN = "api/appapi/BlackBoxLoginnew?"

    const val GET_VEHICLE_COUNT = "api/APPApi/VehiclesStatusGraphAPP?custid="

    const val GET_FLEET_UTILIZATION_DATA = "api/Adminapi/VehicleUtilizationChart?custid="

    const val GET_FUEL_MIELAGE_CHART = "api/customapi/mileageanalysis?custid="

    const val GET_SPEED_ANALYSIS = "api/ReportsApi/GetOverSpeedGraphReport?custid="

    const val GET_NOTIFICATION_LIST = "api/pumas/getnotificationshistory/?custid="

    const val GET_VEHICLE_LIVE_STATUS = "api/AppApi/Livestatus?"

    const val GET_VEHICLE_DETAILS = "AppApi/VehicleStatus?"

    const val GET_DAILY_STATUS_REPORT = "api/Report/DailyNew?"

    const val GET_DISTANCE_REPORT = "api/AddOnAPI/DistanceReport?"

    const val GET_OVER_SPEED_REPORT = "api/Reportsapi/GetOverSpeedReport?"

    const val GET_MONTHLY_REPORT = "api/Reportsapi/GetMonthlyReport?"

    const val GET_STOPPAGE_REPORT = "api/Reportsapi/GetallstoppageReport?"

    const val GET_OVER_STOPPAGE_REPORT = "api/Reportsapi/GetOverStoppageReport?"

    const val GET_DRIVING_LIMIT_REPORT = "api/ReportsApi/GetDrivingLimitReport?"

    const val GET_NO_DRIVING_REPORT = "api/ReportsApi/GetNoDrivingHours?"

    const val GET_DRIVERS_COMPARISON = "api/ReportsApi/getDrMonthComparison?"

    const val GET_DRIVERS_TO_DRIVERS_COMPARISON = "api/ReportsApi/getDriverComparison?"

    const val GET_ROUTE_PLAYBACK = "api/MapAPI/GetPlayBackDataResult?"

    const val GET_ROUTE_PLAYBACK_DRIVER_BEHAVIOUR = "api/APPAPI/GetDBPlayBackDataResult?"

    const val GET_DRIVER_BEHAVIOUR_DASHBOARD = "api/ReportsApi/getDriverGraphPer?custid="

    const val GET_HARSH_BREAK_REPORT = "api/ReportsApi/GetDrBHarshBraking?"

    const val GET_HARSH_ACCELERATION_REPORT = "api/ReportsApi/GetDrBHarshAcceleration?"

    const val GET_RASH_TURN_REPORT = "api/ReportsApi/GetDrBRashTurn?"

    const val GET_TOTAL_VOILATION_REPORT = "api/ReportsApi/getTotalviolationDonut?custid="

    const val GET_DRIVER_RANKING = "api/ReportsApi/GetDriversRanking?custid="

    const val GET_CUSTOMER_CARE_COMPAINT_NUMBERS = "api/CommonApi/GetHelpLineNumber?custID="

    const val GET_FUEL_FILLING_REPORT = "api/CustomAPI/FuelFillingReport?"

    const val GET_FUEL_THEFT_REPORT = "api/CustomAPI/FuelTheftReport?"

    const val GET_FUEL_ROD_DISCONNECTIONS_REPORT = "api/ReportsAPI/GetConsolidatedFuelDisconReport?"

    const val GET_TEMP_REPORT = "api/ReportsApi/GetRefTempReport?"

    const val GET_TEMP_DETAIL_REPORT = "api/ReportsApi/GetRefTempDetailReport?"

    const val GET_WORKING_HOUR_REPORT = "api/ReportsAPI/GetWorkingHoursReport?"

    const val GET_CONSOLIDATE_VOILATION_REPORT = "api/ReportsApi/ConsolidatedViolation?"

    const val GET_VEHICLES_ON_MAP = "api/AppApi/VehicleonmapLiveStatus?"

}