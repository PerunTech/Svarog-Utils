/*******************************************************************************
 * Copyright (c),  2017 TIBRO DOOEL Skopje
 *******************************************************************************/

package com.prtech.svarog.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.prtech.svarog.CodeList;
import com.prtech.svarog.I18n;
import com.prtech.svarog.Sv;
import com.prtech.svarog.SvConf;
import com.prtech.svarog.SvCore;
import com.prtech.svarog.SvException;
import com.prtech.svarog.SvFileStore;
import com.prtech.svarog.SvLink;
import com.prtech.svarog.SvNote;
import com.prtech.svarog.SvParameter;
import com.prtech.svarog.SvReader;
import com.prtech.svarog.SvSequence;
import com.prtech.svarog.svCONST;
import com.prtech.svarog_common.DbDataArray;
import com.prtech.svarog_common.DbDataObject;
import com.prtech.svarog_common.DbQueryExpression;
import com.prtech.svarog_common.DbQueryObject;
import com.prtech.svarog_common.DbQueryObject.DbJoinType;
import com.prtech.svarog_common.DbQueryObject.LinkType;
import com.prtech.svarog_common.DbSearch.DbLogicOperand;
import com.prtech.svarog_common.DbSearchCriterion;
import com.prtech.svarog_common.DbSearchCriterion.DbCompareOperand;
import com.prtech.svarog_common.DbSearchExpression;
import com.prtech.svarog_common.SvCharId;

/**
 * Helper class for access/search of data
 * 
 * @author TIBRO_001
 *
 */

public class Reader {

	static final int COMMIT_COUNT = 1000;
	static final Logger log4j = LogManager.getLogger(Reader.class.getName());


	/**
	 * Method that returns DbDataArray of ORG_UNITS linked per user
	 * 
	 * @param svr SvReader instance
	 * 
	 * @throws SvException
	 * @return DbDataObject
	 */
	public DbDataArray getAllOrgUnitsPerUser(SvReader svr) throws SvException {
		DbDataArray dboOrgUnitsPerUser = null;
		DbDataObject dboUser = SvReader.getUserBySession(svr.getSessionId());
		if (dboUser != null) {
			DbDataObject linkPoaUserOrgUnit = SvReader.getLinkType(Sv.POA, svCONST.OBJECT_TYPE_USER,
					svCONST.OBJECT_TYPE_ORG_UNITS);
			dboOrgUnitsPerUser = svr.getObjectsByLinkedId(dboUser.getObjectId(), linkPoaUserOrgUnit, null, 0, 0);
		}
		return dboOrgUnitsPerUser;
	}



	/**
	 * Helper method that converts dbDataArray into List<String> for a specific
	 * database column from STRING/NVARCHAR data type
	 * 
	 * @param dbArr
	 * @param fieldName
	 * @return
	 */
	public List<String> convertDbDataArrayIntoListForSpecificField(DbDataArray dbArr, String fieldName) {
		List<String> result = new ArrayList<>();
		if (!dbArr.isEmpty()) {
			for (DbDataObject tempDbAnimalClass : dbArr.getItems()) {
				if (!result.contains(tempDbAnimalClass.getAsString(fieldName))) {
					result.add(tempDbAnimalClass.getAsString(fieldName));
				}
			}
		}
		return result;
	}

	/***
	 * Helper methods that processes intersection between two different lists
	 * 
	 * @param A
	 * @param B
	 * @return
	 */
	private List<String> intersect(List<String> A, List<String> B) {
		List<String> rtnList = new LinkedList<>();
		for (String dto : A) {
			if (B.contains(dto)) {
				rtnList.add(dto);
			}
		}
		return rtnList;
	}

	public DbCompareOperand autoSetTheDbCompareOperandParam(String columnName, String columnValue, SvReader svr)
			throws SvException {
		DbCompareOperand compareOperand = DbCompareOperand.ILIKE;
		DbDataArray fieldsResult = findDataPerSingleFilter(Sv.FIELD_NAME.toString(), columnName, DbCompareOperand.EQUAL,
				svCONST.OBJECT_TYPE_FIELD, svr);
		if (fieldsResult != null) {
			DbDataObject dboField = fieldsResult.get(0);
			if (dboField != null && dboField.getVal(Sv.CODE_LIST_ID) != null) {
				compareOperand = DbCompareOperand.EQUAL;
			} else if (columnValue.matches("[0-9]+")) {
				compareOperand = DbCompareOperand.LIKE;
			}
		}
		return compareOperand;
	}

	public DbDataArray findDataPerSingleFilter(String columnName, String columnValue, DbCompareOperand compareOperand,
			Long objTypeId, SvReader svr) throws SvException {
		return findDataPerSingleFilter(columnName, columnValue, compareOperand, objTypeId, 0, svr);
	}

	public DbDataArray findDataPerSingleFilter(String columnName, String columnValue, DbCompareOperand compareOperand,
			Long objTypeId, Integer rowLimit, SvReader svr) throws SvException {
		return findDataPerSingleFilter(columnName, columnValue, compareOperand, objTypeId, rowLimit, null, svr);
	}

	public DbDataArray findDataPerSingleFilter(String columnName, String columnValue, DbCompareOperand compareOperand,
			Long objTypeId, Integer rowLimit, DateTime refDate, SvReader svr) throws SvException {
		DbDataArray result = null;
		DbSearchCriterion cr1 = null;
		if (compareOperand.equals(DbCompareOperand.LIKE)) {
			cr1 = new DbSearchCriterion(columnName, compareOperand, columnValue + Svu.PERCENT_OPERATOR);
		} else if (compareOperand.equals(DbCompareOperand.ILIKE)) {
			cr1 = new DbSearchCriterion(columnName, compareOperand,
					Svu.PERCENT_OPERATOR + columnValue + Svu.PERCENT_OPERATOR);
		} else {
			cr1 = new DbSearchCriterion(columnName, compareOperand, columnValue);
		}
		result = svr.getObjects(new DbSearchExpression().addDbSearchItem(cr1), objTypeId, refDate, rowLimit, 0);
		return result;
	}


	/**
	 * Method that returns data in grid type Json format.
	 * 
	 * @param dbArray
	 * 
	 * @param tableName Name of the table
	 * @return
	 * @throws SvException
	 */
	public String convertDbDataArrayToGridJson(DbDataArray dbArray, String tableName, boolean skipRepoFields,
			SvReader svr) throws SvException {
		return convertDbDataArrayToGridJson(dbArray, tableName, skipRepoFields, null, null, svr);
	}

	/**
	 * Method that returns data in grid type Json format.
	 * 
	 * @param dbArray
	 * 
	 * @param tableName Name of the table
	 * @return
	 * @throws SvException
	 */
	public String convertDbDataArrayToGridJson(DbDataArray dbArray, String tableName, boolean skipRepoFields,
			String fieldNameToSortBy, String sortAscOrDesc, SvReader svr) throws SvException {
		String result = Svu.EMPTY_ARRAY_STRING;
		if (fieldNameToSortBy != null && !dbArray.getItems().isEmpty()) {
			dbArray.getSortedItems(fieldNameToSortBy, true);
		}
		org.json.simple.JSONArray resultJsonArray = null;
		if (sortAscOrDesc == null || sortAscOrDesc.equals("ASC")) {
			resultJsonArray = convertDbDataArrayToJSONArrayAscending(dbArray, tableName, skipRepoFields, svr);
		} else if (sortAscOrDesc != null && sortAscOrDesc.equals("DESC")) {
			resultJsonArray = convertDbDataArrayToJSONArrayDescending(dbArray, tableName, skipRepoFields, svr);
		}
		if (!resultJsonArray.isEmpty()) {
			result = resultJsonArray.toJSONString();
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	public LinkedHashMap<String, JsonElement> getDbDataObjectsAsLinkedHashMap(DbDataObject dbo, String tableName,
			boolean skipRepoFields, SvReader svr) throws SvException {
		DbDataObject dboField = null;
		String localeId = Sv.EMPTY_STRING;
		JsonObject convertedJObj = dbo.toJson().getAsJsonObject(dbo.getClass().getCanonicalName());
		LinkedHashMap<String, JsonElement> lhmObj = new LinkedHashMap<>();
		if (svr != null)
			localeId = svr.getUserLocaleId(svr.getInstanceUser());
		for (Entry<String, JsonElement> tempConverted : convertedJObj.entrySet()) {
			if (!tempConverted.getKey().equals("values")) {
				if (!skipRepoFields) {
					lhmObj.put(tableName + "." + tempConverted.getKey().toUpperCase(), tempConverted.getValue());
				}
			} else {
				JsonArray jsonArray = tempConverted.getValue().getAsJsonArray();
				for (JsonElement je : jsonArray) {
					for (Entry<String, JsonElement> value : je.getAsJsonObject().entrySet()) {
						// check if field has flag Sv.SV_ISLABEL:true
						if (svr != null) {
							dboField = SvReader.getFieldByName(tableName, value.getKey().toUpperCase());
							if (dboField != null && dboField.getVal(Sv.SV_ISLABEL) != null
									&& dboField.getVal(Sv.SV_ISLABEL).equals(true)) {
								lhmObj.put(tableName + "." + value.getKey().toUpperCase() + "_CODE", value.getValue());
								StringBuilder sb = new StringBuilder(
										"\"" + (I18n.getText(localeId, value.getValue().toString().replace("\"", "")))
												+ "\"");
								lhmObj.put(tableName + "." + value.getKey().toUpperCase(),
										new JsonParser().parse(sb.toString()));
							} else {
								lhmObj.put(tableName + "." + value.getKey().toUpperCase(), value.getValue());
							}
						} else {
							lhmObj.put(tableName + "." + value.getKey().toUpperCase(), value.getValue());
						}
					}
				}
			}
		}
		return lhmObj;
	}

	public org.json.simple.JSONArray convertDbDataArrayToJSONArrayDescending(DbDataArray dbArray, String tableName,
			Boolean skipRepoFields, SvReader svr) throws SvException {
		org.json.simple.JSONArray resultJsonArray = new org.json.simple.JSONArray();
		for (int i = dbArray.size() - 1; i >= 0; i--) {
			DbDataObject dbo = dbArray.get(i);
			LinkedHashMap<String, JsonElement> lhmObj = getDbDataObjectsAsLinkedHashMap(dbo, tableName, skipRepoFields,
					svr);
			resultJsonArray.add(lhmObj);
		}
		return resultJsonArray;
	}

	public org.json.simple.JSONArray convertDbDataArrayToJSONArrayAscending(DbDataArray dbArray, String tableName,
			Boolean skipRepoFields, SvReader svr) throws SvException {
		org.json.simple.JSONArray resultJsonArray = new org.json.simple.JSONArray();
		for (DbDataObject dbo : dbArray.getItems()) {
			LinkedHashMap<String, JsonElement> lhmObj = getDbDataObjectsAsLinkedHashMap(dbo, tableName, skipRepoFields,
					svr);
			resultJsonArray.add(lhmObj);
		}
		return resultJsonArray;
	}

	public org.json.simple.JSONArray convertArrayListToJSONArray(ArrayList<String> inputArray) throws SvException {
		org.json.simple.JSONArray resultJsonArray = new org.json.simple.JSONArray();
		for (int i = 0; i < inputArray.size(); i++) {
			resultJsonArray.add(inputArray.get(i));
		}
		return resultJsonArray;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public String convertDbDataArrayToGridJsonDescending(DbDataArray dbArray, String tableName, boolean skipRepoFields,
			SvReader svr) throws SvException {
		String result = "[]";
		DbDataObject dboField = null;
		String localeId = Sv.EMPTY_STRING;
		if (svr != null)
			localeId = svr.getUserLocaleId(svr.getInstanceUser());
		org.json.simple.JSONArray resultJsonArray = new org.json.simple.JSONArray();
		for (int i = dbArray.size() - 1; i > 0; i--) {
			DbDataObject dbo = dbArray.get(i);
			JsonObject convertedJObj = dbo.toJson().getAsJsonObject(dbo.getClass().getCanonicalName());
			LinkedHashMap<String, JsonElement> lhmObj = new LinkedHashMap<>();
			for (Entry<String, JsonElement> tempConverted : convertedJObj.entrySet()) {
				if (!tempConverted.getKey().equals("values")) {
					if (!skipRepoFields) {
						lhmObj.put(tableName + "." + tempConverted.getKey().toUpperCase(), tempConverted.getValue());
					}
				} else {
					JsonArray jsonArray = tempConverted.getValue().getAsJsonArray();
					for (JsonElement je : jsonArray) {
						for (Entry<String, JsonElement> value : je.getAsJsonObject().entrySet()) {
							// check if field has flag Sv.SV_ISLABEL:true
							if (svr != null) {
								dboField = SvReader.getFieldByName(tableName, value.getKey().toUpperCase());
								if (dboField != null && dboField.getVal(Sv.SV_ISLABEL) != null
										&& dboField.getVal(Sv.SV_ISLABEL).equals(true)) {
									lhmObj.put(tableName + "." + value.getKey().toUpperCase() + "_CODE",
											value.getValue());
									StringBuilder sb = new StringBuilder("\""
											+ (I18n.getText(localeId, value.getValue().toString().replace("\"", "")))
											+ "\"");
									lhmObj.put(tableName + "." + value.getKey().toUpperCase(),
											new JsonParser().parse(sb.toString()));
								} else {
									lhmObj.put(tableName + "." + value.getKey().toUpperCase(), value.getValue());
								}
							} else {
								lhmObj.put(tableName + "." + value.getKey().toUpperCase(), value.getValue());
							}
						}
					}
				}
			}
			resultJsonArray.add(lhmObj);
		}
		if (!resultJsonArray.isEmpty()) {
			result = resultJsonArray.toJSONString();
		}
		return result;
	}

	public String convertDbDataArrayToGridJson(DbDataArray dbArray, String tableName) throws SvException {
		return convertDbDataArrayToGridJson(dbArray, tableName, null);
	}

	public String convertDbDataArrayToGridJson(DbDataArray dbArray, String tableName, SvReader svr) throws SvException {
		return convertDbDataArrayToGridJson(dbArray, tableName, false, svr);
	}


	/**
	 * Method for calculating precentage
	 * 
	 * @param num      number of total number
	 * @param totalNum total number
	 * @return
	 */
	public Integer calculatePrecentage(Integer num, Integer totalNum) {
		double precentage = (double) num / totalNum;
		double resultPrecentage = precentage * 100;
		Integer totalResult = (int) resultPrecentage;
		return totalResult;
	}


	// USER METHODS

	/**
	 * Method that returns all the data for specific user
	 * 
	 * @param dboUser    DbDataObject of user
	 * 
	 * @param contactObj DbDataObject of CONTACT_DATA type
	 * 
	 * @throws SvException
	 * @return String
	 */
	public String getUserFullData(DbDataObject dboUser, DbDataObject contactObj) {

		String result = Sv.EMPTY_STRING;
		String userName = Sv.EMPTY_STRING;
		String userType = Sv.EMPTY_STRING;
		String userFirstName = Sv.EMPTY_STRING;
		String userLastName = Sv.EMPTY_STRING;
		String userEmail = Sv.EMPTY_STRING;
		String pin = Sv.EMPTY_STRING;
		String streetType = Sv.EMPTY_STRING;
		String streetName = Sv.EMPTY_STRING;
		String houseNumber = Sv.EMPTY_STRING;
		String postalCode = Sv.EMPTY_STRING;
		String city = Sv.EMPTY_STRING;
		String state = Sv.EMPTY_STRING;
		String phoneNumber = Sv.EMPTY_STRING;
		String mobilePhoneNumber = Sv.EMPTY_STRING;
		String fax = Sv.EMPTY_STRING;
		String email = Sv.EMPTY_STRING;

		if (dboUser.getVal(Sv.USER_NAME) != null)
			userName = dboUser.getAsString(Sv.USER_NAME);
		if (dboUser.getVal(Svu.USER_TYPE) != null)
			userType = dboUser.getAsString(Svu.USER_TYPE);
		if (dboUser.getVal(Sv.FIRST_NAME) != null)
			userFirstName = dboUser.getAsString(Sv.FIRST_NAME);
		if (dboUser.getVal(Svu.LAST_NAME) != null)
			userLastName = dboUser.getAsString(Svu.LAST_NAME);
		if (dboUser.getVal(Sv.E_MAIL) != null)
			userEmail = dboUser.getAsString(Sv.E_MAIL);
		if (dboUser.getVal(Svu.PIN) != null)
			pin = dboUser.getAsString(Svu.PIN);
		if (contactObj != null && !contactObj.getObjectId().equals(0L)) {
			if (contactObj.getVal("STREET_TYPE") != null)
				streetType += contactObj.getAsString("STREET_TYPE");
			if (contactObj.getVal("STREET_NAME") != null)
				streetName += contactObj.getAsString("STREET_NAME");
			if (contactObj.getVal("HOUSE_NUMBER") != null) {
				houseNumber += contactObj.getAsString("HOUSE_NUMBER");
			}
			if (contactObj.getVal("POSTAL_CODE") != null)
				postalCode = contactObj.getAsString("POSTAL_CODE");
			if (contactObj.getVal("CITY") != null)
				city = contactObj.getAsString("CITY");
			if (contactObj.getVal("STATE") != null)
				state = contactObj.getAsString("STATE");
			if (contactObj.getVal("PHONE_NUMBER") != null)
				phoneNumber = contactObj.getAsString("PHONE_NUMBER");
			if (contactObj.getVal("MOBILE_NUMBER") != null)
				mobilePhoneNumber = contactObj.getAsString("MOBILE_NUMBER");
			if (contactObj.getVal("FAX") != null)
				fax = contactObj.getAsString("FAX");
			if (contactObj.getVal("EMAIL") != null)
				email = contactObj.getAsString("EMAIL");
		}

		result = "userObjId:" + dboUser.getObjectId().toString() + ";userName:" + userName + ";userType:" + userType
				+ ";userFirstName:" + userFirstName + ";userLastName:" + userLastName + ";userEmail:" + userEmail
				+ ";pinNumber:" + pin + ";streetType:" + streetType + ";streetName:" + streetName + ";houseNumber:"
				+ houseNumber + ";postalCode:" + postalCode + ";city:" + city + ";state:" + state + ";phoneNumber:"
				+ phoneNumber + ";mobilePhoneNumber:" + mobilePhoneNumber + ";fax:" + fax + ";email:" + email;

		return result;
	}

	
	// LABEL MANAGEMENT METHODS

	/**
	 * Method that returns appropriate label translation for field/column
	 * 
	 * @param codeListId object_id of the appropriate code list
	 * 
	 * @param value      code value of the code list item
	 * 
	 * @param localeId   locale_id
	 * @param svr        SvReader instance
	 * 
	 * @throws SvException
	 * @return String of the translated code value
	 */
	public String translateCodeValueForField(Long codeListId, String value, String localeId, SvReader svr)
			throws SvException {
		String translatedCodeValue = Svu.NOT_AVAILABLE_NA;
		DbDataObject codeListObj = svr.getObjectById(codeListId, svCONST.OBJECT_TYPE_CODE, null);
		if (codeListObj != null && value != null && !value.trim().equals("")) {
			DbDataArray codeListItems = svr.getObjectsByParentId(codeListObj.getObjectId(), svCONST.OBJECT_TYPE_CODE,
					null, 0, 0);
			if (codeListItems != null && !codeListItems.getItems().isEmpty()) {
				for (DbDataObject currCodeListItem : codeListItems.getItems()) {
					if (currCodeListItem.getVal(Sv.CODE_VALUE) != null
							&& currCodeListItem.getVal(Sv.CODE_VALUE).equals(value)
							&& currCodeListItem.getVal(Sv.LABEL_CODE) != null) {
						translatedCodeValue = I18n.getText(localeId, currCodeListItem.getAsString(Sv.LABEL_CODE));
						break;
					}
				}
			}
		}
		return translatedCodeValue;
	}

	/**
	 * Method that checks and returns the label if is already installed
	 * 
	 * @param labelCode the labelCode of the label we look for
	 * @param localeId  localeId of the label we look for (for the same labelCode)
	 * @param svReader  SvReader instance
	 * @throws SvException
	 * @return DbDataObject if exists, else null
	 * 
	 */
	public DbDataObject getLabel(String labelCode, String localeId, SvReader svReader) throws SvException {

		DbDataObject result = null;
		DbDataArray searchResult = new DbDataArray();
		DbSearchExpression findLabel = new DbSearchExpression();
		DbSearchCriterion byLabelCode = new DbSearchCriterion(Sv.LABEL_CODE.toString(), DbCompareOperand.EQUAL, labelCode);
		DbSearchCriterion byLocaleId = new DbSearchCriterion(Sv.LOCALE_ID, DbCompareOperand.EQUAL, localeId);
		findLabel.addDbSearchItem(byLabelCode);
		findLabel.addDbSearchItem(byLocaleId);
		searchResult = svReader.getObjects(findLabel, svCONST.OBJECT_TYPE_LABEL, null, 0, 0);
		if (searchResult.getItems().isEmpty()) {
			searchResult = svReader.getObjects(findLabel, svCONST.OBJECT_TYPE_LABEL, new DateTime(), 0, 0);
		}
		if (!searchResult.getItems().isEmpty()) {
			result = searchResult.getItems().get(0);
		}
		return result;
	}

	/**
	 * Method to feSvh appropriate label translation for table id/value/locale id
	 * 
	 * @param tableId   object_type of the table
	 * 
	 * @param fieldName name of the column
	 * 
	 * @param value     code_list value
	 * 
	 * @param localeId  locale_id
	 * 
	 * @param svr       SvReader instance
	 * 
	 * @throws SvException
	 * @return String of the translated value
	 */
	public String decodeCodeValue(Long tableId, String fieldName, String value, String localeId, SvReader svr)
			throws SvException {
		String translatedValue = Svu.NOT_AVAILABLE_NA;
		DbDataObject dboTable = svr.getObjectById(tableId, svCONST.OBJECT_TYPE_TABLE, null);
		if (dboTable != null && value != null) {
			DbDataArray fields = svr.getObjectsByParentId(dboTable.getObjectId(), svCONST.OBJECT_TYPE_FIELD, null, 0,
					0);
			DbDataObject fieldObj = null;
			if (fields != null && !fields.getItems().isEmpty()) {
				for (DbDataObject currField : fields.getItems()) {
					if (currField.getVal(Sv.FIELD_NAME).equals(fieldName)) {
						fieldObj = currField;
						break;
					}
				}
				if (fieldObj != null && fieldObj.getVal(Sv.CODE_LIST_ID) != null) {
					Long codeListId = fieldObj.getAsLong(Sv.CODE_LIST_ID);
					translatedValue = translateCodeValueForField(codeListId, value, localeId, svr);
				}
			}
		}
		return translatedValue;
	}

	
	/**
	 * Method that checks if certain Population or Sample object status can be
	 * updated/changed
	 * 
	 * @param dbo
	 * @param nextStatus
	 * @param svr
	 * @return
	 * @throws SvException
	 */
	public Boolean checkCanChangeStatus(DbDataObject dbo, String nextStatus, SvReader svr) throws SvException {
		Boolean result = true;
		if (dbo.getStatus().equals(nextStatus)) {
			result = false;
		}
		return result;
	}


	// ORG_UNIT METHODS

	/**
	 * Method that returns parent org units per org unit
	 * 
	 * @param dbArr
	 * @param dboOrgUnit
	 * @param svr
	 * @return
	 * @throws Exception
	 * @throws SvException
	 */
	public DbDataArray getAppropriateParentOrgUnits(DbDataArray dbArr, DbDataObject dboOrgUnit, SvReader svr)
			throws Exception, SvException {
		if (dbArr.getItems().isEmpty()) {
			dbArr.addDataItem(dboOrgUnit);
		}
		if (dboOrgUnit.getVal(Svu.PARENT_OU_ID) != null) {
			DbDataObject dboParentOrgUnit = svr.getObjectById(dboOrgUnit.getAsLong(Svu.PARENT_OU_ID),
					svCONST.OBJECT_TYPE_ORG_UNITS, null);
			if (dboParentOrgUnit != null && dboParentOrgUnit.getVal(Sv.NAME) != null
					&& !dboParentOrgUnit.getAsString(Sv.NAME).equalsIgnoreCase(Svu.HEADQUARTER)) {
				dbArr.addDataItem(dboParentOrgUnit);
				return getAppropriateParentOrgUnits(dbArr, dboParentOrgUnit, svr);
			}
		}
		return dbArr;
	}

	/**
	 * Method that returns initial linked org unit and all sub org units linked to
	 * certain user
	 * 
	 * @param dboUser    User DbDataObject
	 * @param dboOrgUnit Selected/Initial Org Unit DbDataObject
	 * @param svr        SvReader instance
	 * @return
	 * @throws SvException
	 */
	public DbDataArray getAppropriateSubOrgUnits(DbDataObject dboUser, DbDataObject dboOrgUnit, DbDataObject dbLinkType,
			SvReader svr) throws SvException {
		DbDataArray arrSubOrgUnits = new DbDataArray();
		if (dboOrgUnit.getVal(Sv.NAME) != null) {
			Long orgUnitObjId = dboOrgUnit.getObjectId();
			DbDataArray arrLinkedOrgUnitsPerUser = svr.getObjectsByLinkedId(dboUser.getObjectId(),
					dboUser.getObjectType(), dbLinkType, svCONST.OBJECT_TYPE_ORG_UNITS, false, new DateTime(), 0, 0);
			if (!arrLinkedOrgUnitsPerUser.getItems().isEmpty()) {
				for (DbDataObject tempOrgUnit : arrLinkedOrgUnitsPerUser.getItems()) {
					if (tempOrgUnit.getAsLong(Svu.PARENT_OU_ID) == orgUnitObjId) {
						arrSubOrgUnits.addDataItem(tempOrgUnit);
					}
				}
			}
		}
		return arrSubOrgUnits;
	}

	/**
	 * Method that returns SdiUnit object_id by UNIT_ID
	 * 
	 * @param externalUnitId UNIT_ID of the unit
	 * @param uniSvlass      class of the unit
	 * @param svc            SvCore instance
	 * @throws SvException
	 * @return Long- object_id of the unit
	 * 
	 */
	public Long findAppropriateSdiUnitByUnitId(String externalUnitId, String unitClass, SvCore svc) {
		SvReader svr = null;
		Long result = null;
		try {
			svr = new SvReader(svc);
			DbSearchCriterion cr1 = new DbSearchCriterion(Svu.UNIT_ID, DbCompareOperand.EQUAL, externalUnitId);
			DbSearchCriterion cr2 = null;
			
			switch (unitClass) 
			{
			case "REGION":
				cr2 = new DbSearchCriterion(Svu.UNIT_CLASS, DbCompareOperand.EQUAL, "1");
				break;
			case "MUNICIPALITY":
				cr2 = new DbSearchCriterion(Svu.UNIT_CLASS, DbCompareOperand.EQUAL, "2");
				break;
			case "VILLAGE":
				cr2 = new DbSearchCriterion(Svu.UNIT_CLASS, DbCompareOperand.EQUAL, "3");
				break;
			default:
				break;
			}
			DbDataArray dbArray = svr.getObjects(new DbSearchExpression().addDbSearchItem(cr1).addDbSearchItem(cr2),
					svCONST.OBJECT_TYPE_SDI_UNITS, null, 0, 0);
			if (dbArray != null && !dbArray.getItems().isEmpty() && dbArray.getItems().get(0).getObjectId() != null) {
				result = dbArray.getItems().get(0).getObjectId();
				log4j.trace("SDI_UNIT with unit_id code: " + externalUnitId + " already exists");
			}
		} catch (SvException e) {
			log4j.error(e.getFormattedMessage(), e);
		} finally {
			if (svr != null) {
				svr.release();
			}
		}
		return result;
	}

	/**
	 * Method for feSvhing all municipalities
	 * 
	 * @param svr SvReader instance
	 * @return DbDataArray
	 */
	public DbDataArray getMunicipalities(SvReader svr) {
		DbDataArray result = new DbDataArray();
		try {
			DbSearchExpression dbse = new DbSearchExpression();
			DbSearchCriterion sc1 = new DbSearchCriterion(Svu.UNIT_CLASS, DbCompareOperand.EQUAL, "2");
			dbse.addDbSearchItem(sc1);

			result = svr.getObjects(dbse, svCONST.OBJECT_TYPE_SDI_UNITS, null, 0, 0);
		} catch (Exception e) {
			log4j.error("Failed feSvhing municipalitiers with: " + e.getMessage());
		}
		return result;
	}

	public DbDataArray getOrgUnitDependOnParentExternalId(Long betweenStart, Long betweenEnd, SvReader svr)
			throws SvException {
		DbSearchCriterion cr1 = new DbSearchCriterion(Svu.EXTERNAL_ID, DbCompareOperand.BETWEEN, betweenStart,
				betweenEnd);
		DbDataArray result = svr.getObjects(new DbSearchExpression().addDbSearchItem(cr1),
				svCONST.OBJECT_TYPE_ORG_UNITS, null, 0, 0);
		return result;
	}

	

	// OTHER METHODS

	/**
	 * Help method that feSvh all dependent data for each region/munic/commun
	 * Purpose: to load appropriate list of dependent dropdown
	 * 
	 * @param parenSvode:   code_value of parent code list item
	 * @param codeListName: name of the code list, which should be accessed
	 * @throws SvException
	 */
	public DbDataArray searchForDependentMunicCommunVillage(String parenSvode, String codeListName, SvReader svr)
			throws SvException {
		DbDataArray itemsFound = null;
		DbSearchExpression srchExpr = new DbSearchExpression();
		DbSearchCriterion filterByParenSvodeValue = new DbSearchCriterion(Sv.PARENT_CODE_VALUE, DbCompareOperand.EQUAL,
				codeListName);
		DbSearchCriterion filterByCodeValue = new DbSearchCriterion(Sv.CODE_VALUE, DbCompareOperand.LIKE,
				parenSvode + Svu.PERCENT_OPERATOR);
		srchExpr.addDbSearchItem(filterByParenSvodeValue).addDbSearchItem(filterByCodeValue);
		DbDataArray searchResult = svr.getObjects(srchExpr, svCONST.OBJECT_TYPE_CODE, null, 0, 0);
		if (!searchResult.getItems().isEmpty()) {
			itemsFound = searchResult;
			String userLocale = svr.getUserLocaleId(svr.getInstanceUser());
			for (DbDataObject tempCodeItemFound : itemsFound.getItems()) {
				String translatedCodeItem = I18n.getText(userLocale, tempCodeItemFound.getAsString(Sv.LABEL_CODE));
				tempCodeItemFound.setVal(Svu.LBL_TRANSL, translatedCodeItem);
			}
		}
		return itemsFound;
	}

	/**
	 * Method that checks and return if existing form_field_type by label code
	 * 
	 * @param labelCode the label code of the form_field_type object
	 * @param svReader  SvReader instance
	 * @throws SvException
	 * @returns DbDataObject if found some, if no returns null
	 */
	public DbDataObject getFormFieldType(String labelCode, SvReader svReader) throws SvException {

		DbDataObject result = null;

		DbSearchExpression getFormType = new DbSearchExpression();
		DbSearchCriterion filterByLabel = new DbSearchCriterion(Sv.LABEL_CODE.toString(), DbCompareOperand.EQUAL, labelCode);
		getFormType.addDbSearchItem(filterByLabel);
		DbDataArray searchResult = svReader.getObjects(getFormType, svCONST.OBJECT_TYPE_FORM_FIELD_TYPE, null, 0, 0);

		if (!searchResult.getItems().isEmpty()) {
			result = searchResult.getItems().get(0);
		}
		return result;
	}



	/**
	 * Method that checks if SvLink exists between two DB objects
	 * 
	 * @param dbo1     The first DbDataObject
	 * 
	 * @param dbo2     The second DbDataObject
	 * 
	 * @param linkName name of the link type
	 * 
	 * @param refDate  The reference date on which we want to get the data set
	 * 
	 * @param svr      SvReader instance
	 * 
	 * @throws SvException
	 * @return Boolean
	 */
	public boolean checkIfLinkExists(DbDataObject dbo1, DbDataObject dbo2, String linkName, DateTime refDate,
			SvReader svr) throws SvException {
		Boolean result = false;
		DbDataObject dbLink = SvLink.getLinkType(linkName, dbo1.getObjectType(), dbo2.getObjectType());
		DbDataArray allItems = svr.getObjectsByLinkedId(dbo1.getObjectId(), dbLink, refDate, 0, 0);
		for (DbDataObject dbo : allItems.getItems()) {
			if (dbo.getObjectId().equals(dbo2.getObjectId())) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * Method that returns DbDataArray for specific criteria and object type
	 * 
	 * @param hashMapColumnNameAndValue LinkedHashMap with key:ColumnName and
	 *                                  value:FieldValue
	 * @param objType                   object type
	 * @param svr                       SvReader instance
	 * @return
	 * @throws SvException
	 */
	public DbDataArray getDbDataWithCriteria(LinkedHashMap<String, String> criteriaHashMap, Long objType, SvReader svr)
			throws SvException {
		DbDataArray result = new DbDataArray();
		DbSearchCriterion cr1 = null;
		DbSearchExpression dbse = new DbSearchExpression();
		if (criteriaHashMap != null && !criteriaHashMap.isEmpty()) {
			for (Entry<String, String> a : criteriaHashMap.entrySet()) {
				cr1 = new DbSearchCriterion(a.getKey(), DbCompareOperand.EQUAL, a.getValue());
				dbse.addDbSearchItem(cr1);
			}
			result = svr.getObjects(dbse, objType, null, 0, 0);
		}
		return result;
	}

	/**
	 * Method that returns N size shuffled DbDataArray
	 * 
	 * @param arrayToRandomize array to be randomized
	 * @param resultSampleSize size of the output of shuffled DbDataArray (N)
	 * @param svr              SvReader instance
	 * @return shuffled N size DbDataArray
	 */
	public DbDataArray getNRandomElementsFromDbDataArray(DbDataArray arrayToRandomize, Integer resultSampleSize) {
		DbDataArray result = null;
		if (arrayToRandomize != null && !arrayToRandomize.getItems().isEmpty()) {
			result = new DbDataArray();
			Collections.shuffle(arrayToRandomize.getItems());
			result = arrayToRandomize;
		} else {
			log4j.trace("The input array list is null/empty. Nothing to be shuffled.");
		}
		return result;
	}

	
	public String getGeostaSvodeFromOrgUnit(DbDataObject inventoryItemObj, SvReader svr) throws SvException {
		String result = Sv.EMPTY_STRING;
		DbDataObject orgUnitObj = svr.getObjectById(inventoryItemObj.getParentId(), svCONST.OBJECT_TYPE_ORG_UNITS,
				null);
		if (orgUnitObj != null && orgUnitObj.getVal(Svu.EXTERNAL_ID) != null) {
			result = orgUnitObj.getAsString(Svu.EXTERNAL_ID);
		}
		return result;
	}

	/**
	 * Method that gets Link Object if Link exists
	 * 
	 * @param obj_1    Linked Object_Id 1
	 * @param obj_2    Linked Object_Id 2
	 * @param linkType Object type
	 * @param svr
	 * @return DbDataObject - SvLink Object
	 * @throws SvException
	 */
	public DbDataObject getLinkObject(Long obj_1, Long obj_2, Long linkType, SvReader svr) throws SvException {
		return getLinkObject(obj_1, obj_2, linkType, true, svr);
	}

	public DbDataObject getLinkObject(Long obj_1, Long obj_2, Long linkType, Boolean useCache, SvReader svr)
			throws SvException {
		DbDataObject getLink = null;
		DbDataArray resultArr;
		DbSearchCriterion cr1 = new DbSearchCriterion(Svu.LINK_OBJ_ID_1, DbCompareOperand.EQUAL, obj_1);
		DbSearchCriterion cr2 = new DbSearchCriterion(Svu.LINK_OBJ_ID_2, DbCompareOperand.EQUAL, obj_2);
		DbSearchCriterion cr3 = new DbSearchCriterion(Svu.LINK_TYPE_ID, DbCompareOperand.EQUAL, linkType);
		DbSearchExpression dbse = new DbSearchExpression();
		dbse.addDbSearchItem(cr1).addDbSearchItem(cr2).addDbSearchItem(cr3);
		if (useCache) {
			resultArr = svr.getObjects(dbse, svCONST.OBJECT_TYPE_LINK, null, 0, 0);
		} else {
			resultArr = svr.getObjects(dbse, svCONST.OBJECT_TYPE_LINK, new DateTime(), 0, 0);
		}
		if (!resultArr.getItems().isEmpty()) {
			getLink = resultArr.get(0);
		}
		return getLink;
	}

	
	/**
	 * 
	 * @param objToSearch
	 * @param columnToSearch
	 * @param valueToSearch
	 * @param svReader
	 * @return
	 */
	public DbDataObject searchForObject(long objToSearch, String columnToSearch, String valueToSearch,
			SvReader svReader) {
		DbDataArray foundObjects;
		DbDataObject result = null;
		try {
			DbSearchExpression expr = new DbSearchExpression();
			expr.addDbSearchItem(new DbSearchCriterion(columnToSearch, DbCompareOperand.EQUAL, valueToSearch));
			foundObjects = svReader.getObjects(expr, objToSearch, null, 0, 0);
			if (!foundObjects.getItems().isEmpty()) {
				result = foundObjects.get(0);
			}
		} catch (SvException ex) {
			log4j.error("Error in searchForObject: " + ex.getFormattedMessage());
		}
		return result;
	}

	public DbDataArray searchForObjectWithSingleFilter(long objToSearch, String columnToSearch, Object valueToSearch,
			SvReader svr) {
		return searchForObjectWithSingleFilter(objToSearch, columnToSearch, valueToSearch, true, svr);
	}

	public DbDataArray searchForObjectWithSingleFilter(long objToSearch, String columnToSearch, Object valueToSearch,
			Boolean useCache, SvReader svr) {
		DbDataArray foundObjects = new DbDataArray();
		DateTime dtNow = null;
		if (!useCache) {
			dtNow = new DateTime();
		}
		try {
			DbSearchExpression expr = new DbSearchExpression();
			expr.addDbSearchItem(new DbSearchCriterion(columnToSearch, DbCompareOperand.EQUAL, valueToSearch));
			foundObjects = svr.getObjects(expr, objToSearch, dtNow, 0, 0);
		} catch (SvException ex) {
			log4j.error("Error in searchForObject: " + ex.getFormattedMessage());
		}
		return foundObjects;
	}

	/**
	 * Method for getting multi-select field value in List.
	 * 
	 * @param dboObject instance of the object we want to use
	 * @param fieldName name of multi-select field
	 * @return List <String>
	 */
	public List<String> getMultiSelectFieldValueAsList(DbDataObject dbo, String fieldName) {
		List<String> multiSelectResult = new ArrayList<>();
		if (dbo.getVal(fieldName) != null && !dbo.getVal(fieldName).toString().equals("")) {
			String multiValue = dbo.getVal(fieldName).toString();
			multiSelectResult = new ArrayList<String>(Arrays.asList(multiValue.split(",")));
		}
		return multiSelectResult;
	}

	/**
	 * Custom date formatter
	 * 
	 * @param date Date in string format (YYYY-MM-DD)
	 * @return Date in string format (DD.MM.YYYY)
	 */
	public String customDateFormatter(String date) {
		String[] dateForm = date.split("-");
		String result = date;
		if (date != null && !date.equals("")) {
			result = new String();
			String temp = dateForm[dateForm.length - 1];
			dateForm[dateForm.length - 1] = dateForm[0];
			dateForm[0] = temp;

			for (int i = 0; i < dateForm.length; i++) {
				result += dateForm[i];
				if (i != dateForm.length - 1) {
					result += ".";
				}
			}
		}
		return result;
	}

	public Integer getDayDiffBetweenDates(String date1, String date2) {
		Integer result = 0;
		String pattern = Svu.DATE_PATTERN;
		DateTime dateOfFirst = DateTime.parse(date1, DateTimeFormat.forPattern(pattern));
		DateTime dateOfSecond = DateTime.parse(date2, DateTimeFormat.forPattern(pattern));
		result = (int) getDateDiff(dateOfFirst, dateOfSecond, TimeUnit.DAYS);
		return result;
	}
	/**
	 * @param firstDate
	 * @param secondDate
	 * @param timeUnit
	 * @return
	 */
	public static long getDateDiff(DateTime firstDate, DateTime secondDate, TimeUnit timeUnit) {
		long diff = firstDate.toDate().getTime() - secondDate.toDate().getTime();
		return timeUnit.convert(diff, TimeUnit.MILLISECONDS);
	}
	
	public DateTime getDateFromDBField(String dbFieldName, DbDataObject dboToExtractInfoFrom) {
		DateTime result = null;
		if (dboToExtractInfoFrom.getVal(dbFieldName) != null) {
			String pattern = Sv.DATE_PATTERN;
			String departureDate = dboToExtractInfoFrom.getVal(dbFieldName).toString().substring(0, 10);
			result = DateTime.parse(departureDate, DateTimeFormat.forPattern(pattern));
		}
		return result;
	}

	public Integer toAbsoluteValue(Integer num) {
		Integer convertedNum = num;
		String numberToString = String.valueOf(num);
		if (numberToString.contains("-")) {
			String tempAbsoulteValue = numberToString.replace("-", "");
			convertedNum = Integer.valueOf(tempAbsoulteValue.trim());
		}
		return convertedNum;
	}

	// example: DATE_OF_ACTION, NOW()-6MONTHS, NOW()
	/**
	 * 
	 * Method that is used as filter i.e, it gets all objects of given array in
	 * given time interval (between two dates)
	 * 
	 * @param array_Objects  DbDataArray - Array to be filtered
	 * @param date_Field_Obj String - Name of the date field of the objects in the
	 *                       given array
	 * @param date_1         Date 1 (From)
	 * @param date_2         Date 2 (To)
	 * @param use_As_Chek
	 * 
	 * @return filtered DbDataArray
	 */
	public DbDataArray filterObjectsByDateTimeFrame(DbDataArray arrayObjects, String dateFieldObj, DateTime date1,
			DateTime date2, boolean useAsChek) {
		DbDataArray resultObjects = new DbDataArray();
		DateTime convertedDateField = null;
		if (arrayObjects != null && !arrayObjects.getItems().isEmpty()) {
			arrayObjects.getSortedItems(dateFieldObj);
			for (DbDataObject dboObject : arrayObjects.getItems()) {
				if (dboObject.getVal(dateFieldObj) != null) {
					convertedDateField = new DateTime(dboObject.getVal(dateFieldObj).toString());
					if (convertedDateField.isAfter(date1) && convertedDateField.isBefore(date2)) {
						resultObjects.addDataItem(dboObject);
						if (useAsChek) {
							break;
						}
					}
				}
			}
		}
		return resultObjects;
	}

	/**
	 * 
	 * Method that is used as filter i.e, it gets all objects of given array in
	 * given time interval (between two dates). The default second date (Date to) is
	 * NOW()
	 * 
	 * @param array_Objects  DbDataArray - Array to be filtered
	 * @param date_Field_Obj String - Name of the date field of the objects in the
	 *                       given array
	 * @param date_1         Date 1 (From)
	 * @return filtered DbDataArray
	 */
	public DbDataArray filterObjectsByDateTimeFrame(DbDataArray array_Objects, String date_Field_Obj, DateTime date_1) {
		DateTime date_Now = new DateTime();
		return filterObjectsByDateTimeFrame(array_Objects, date_Field_Obj, date_1, date_Now, false);
	}

	/**
	 * Method that returns object by PKID.
	 * 
	 * @param pkid       PKID of the object
	 * @param objectType Object type
	 * @param svr        SvReader instance
	 * @return
	 * @throws SvException
	 */
	public DbDataObject getObjectByPkid(Long pkid, String objectType, SvReader svr) throws SvException {
		DbDataObject resultObj = new DbDataObject();
		DbSearchCriterion cr1 = new DbSearchCriterion(Sv.PKID, DbCompareOperand.EQUAL, pkid);
		DbSearchExpression dbse = new DbSearchExpression();
		dbse.addDbSearchItem(cr1);
		DbDataArray resultArray = svr.getObjectsHistory(dbse, SvReader.getTypeIdByName(objectType), 0, 0);
		if (resultArray != null && !resultArray.getItems().isEmpty()) {
			resultObj = resultArray.get(0);
		}
		return resultObj;
	}

	

	

	public DbDataArray getNotesAccordingParentIdAndNoteName(Long parentId, String noteName, SvReader svr)
			throws SvException {
		DbDataArray arrNotes;
		DbSearchCriterion cr1 = new DbSearchCriterion(Sv.PARENT_ID, DbCompareOperand.EQUAL, parentId);
		DbSearchCriterion cr2 = new DbSearchCriterion(Svu.NOTE_NAME, DbCompareOperand.EQUAL, noteName);
		arrNotes = svr.getObjects(new DbSearchExpression().addDbSearchItem(cr1).addDbSearchItem(cr2),
				svCONST.OBJECT_TYPE_NOTES, new DateTime(), 0, 0);
		return arrNotes;
	}

	

	/**
	 * Method for finding locale id per user, If not set returns default
	 * 
	 * @param svr SvReader instance
	 */

	public static String getLocaleId(SvReader svr) {
		String locale = SvConf.getDefaultLocale();
		try {
			if (svr.getUserLocale(SvReader.getUserBySession(svr.getSessionId())) != null) {
				DbDataObject localeObj = svr.getUserLocale(SvReader.getUserBySession(svr.getSessionId()));
				if (localeObj.getAsString(Sv.LOCALE_ID) != null)
					locale = localeObj.getAsString(Sv.LOCALE_ID);
			}
		} catch (SvException e) {
			log4j.error(e.getFormattedMessage(), e);
		}
		return locale;
	}

	/**
	 * Method for feSvhing svParam as STRING
	 * 
	 * @param dbo   - The object for which we will return the parameter value
	 *              according to label
	 * @param label - LABEL_CODE of SVAROG_PARAM_TYPE object.
	 * @param svr   - SvReader instance
	 * @return String
	 * @throws SvException
	 */
	public String getSvParamAsString(DbDataObject dbo, String label, SvReader svr) throws SvException {
		String result = "";
		try (SvParameter svp = new SvParameter(svr)) {
			result = svp.getParamString(dbo, label);
		}
		return result;
	}

	/**
	 * Simple help method for feSvhing DB object by single filter
	 * 
	 * @param objectType
	 * @param columnName
	 * @param columnValue
	 * @param svr
	 * @return
	 * @throws SvException
	 */
	public DbDataObject searchDbObjectBySingleFilter(Long objectType, String columnName, Object columnValue,
			SvReader svr) {
		return searchDbObjectBySingleFilter(DbCompareOperand.EQUAL, objectType, columnName, columnValue, svr);
	}

	/**
	 * Simple method for searching object by single filter
	 * 
	 * @param objectType
	 * @param columnName
	 * @param value
	 * @param svr
	 * @return
	 */
	public DbDataObject searchDbObjectBySingleFilter(DbCompareOperand operand, Long objectType, String columnName,
			Object value, SvReader svr) {
		DbDataObject dbo = null;
		DbDataArray arrFoundDbObjects = searchDbObjectsBySingleFilter(operand, objectType, columnName, value, svr);
		if (!arrFoundDbObjects.getItems().isEmpty()) {
			dbo = arrFoundDbObjects.get(0);
		}
		return dbo;
	}

	/**
	 * Simple method for searching objects by single filter
	 * 
	 * @param objectType
	 * @param columnName
	 * @param value
	 * @param svr
	 * @return DbDataArray
	 */
	public DbDataArray searchDbObjectsBySingleFilter(DbCompareOperand operand, Long objectType, String columnName,
			Object value, SvReader svr) {
		DbDataArray dbArr = null;
		try {
			DbSearchCriterion cr1 = new DbSearchCriterion(columnName, operand, value);
			dbArr = svr.getObjects(cr1, objectType, null, null, 0);
		} catch (SvException e) {
			log4j.error(e);
		}
		return dbArr;
	}

	private static JsonObject addBoleanParse(JsonObject jsonData, DbDataObject recordObject, String readField,
			String saveField) {
		Boolean tmpB = null;
		try {
			tmpB = (Boolean) recordObject.getVal(readField);
			if (tmpB) { // true
				if ("mk_MK".equalsIgnoreCase(SvConf.getDefaultLocale()))
					jsonData.addProperty(saveField, I18n.getText(SvConf.getDefaultLocale(), "mk.yes"));
				else
					jsonData.addProperty(saveField, I18n.getText(SvConf.getDefaultLocale(), "yes"));
			} else {
				if ("mk_MK".equalsIgnoreCase(SvConf.getDefaultLocale()))
					jsonData.addProperty(saveField, I18n.getText(SvConf.getDefaultLocale(), "mk.no"));
				else
					jsonData.addProperty(saveField, I18n.getText(SvConf.getDefaultLocale(), "no"));
			}
		} catch (Exception e) {
			String tmpStr = null;
			tmpStr = (String) recordObject.getVal(readField);
			if (tmpStr != null) {
				if (tmpStr.equalsIgnoreCase("true") || tmpStr.equalsIgnoreCase("t") || tmpStr.equalsIgnoreCase("y")
						|| tmpStr.equalsIgnoreCase("1") || tmpStr.equalsIgnoreCase("yes")
						|| tmpStr.equalsIgnoreCase(I18n.getText(SvConf.getDefaultLocale(), "mk.yes"))
						|| tmpStr.equalsIgnoreCase(I18n.getText(SvConf.getDefaultLocale(), "yes")))
					tmpB = true;
				if (tmpStr.equalsIgnoreCase("false") || tmpStr.equalsIgnoreCase("f") || tmpStr.equalsIgnoreCase("n")
						|| tmpStr.equalsIgnoreCase("0") || tmpStr.equalsIgnoreCase("no")
						|| tmpStr.equalsIgnoreCase(I18n.getText(SvConf.getDefaultLocale(), "mk.no"))
						|| tmpStr.equalsIgnoreCase(I18n.getText(SvConf.getDefaultLocale(), "no")))
					tmpB = false;

			}
			debugException(e);
		}
		return jsonData;
	}

	/**
	 * procedure to add value to JsonObject one field value from record from table,
	 * this will not process svarog repo fields, this is very similar to
	 * addValueToJsonObject1 but works on data that is part of a query or a simple
	 * table
	 * 
	 * @param jsonData     JsonObject object in which we want to save the data
	 * @param recordObject DbDataObject object from which we read the data
	 * @param tmpField     DbDataObject object from SVAROG_FIELDS that will describe
	 *                     the field that we try to save
	 * @param saveField    String under what name we want to insert the value, if we
	 *                     have query data is saved as TBL0, TBL1, TB2, so with this
	 *                     we translate those with correct table names
	 * @param doTranslate  Boolean if set to TRUE it will translate all label_codes,
	 *                     so it should be TRUE most of the time except in
	 *                     administrative console where we have to set the codes
	 * 
	 * @return JSON String with data from oData Object
	 */
	private static JsonObject addValueToJsonObject2(JsonObject jsonData, DbDataObject recordObject,
			DbDataObject tmpField, String readField, String saveField, Boolean doTranslate, SvReader svr) {
		JsonObject jsonDataForWork = jsonData;
		CodeList cl = null;
		String nameField = tmpField.getAsString(Sv.FIELD_NAME);
		if (processField(nameField))
			try {
				if (recordObject.getVal(readField) == null) {
					return jsonDataForWork;
				}
				switch (tmpField.getAsString(Sv.FIELD_TYPE)) {
				case Sv.NVARCHAR:
					String tmpS = null;
					if (recordObject.getVal(readField) != null)
						tmpS = recordObject.getAsString(readField);
					if (tmpField.getVal(Sv.SV_ISLABEL) != null && tmpField.getVal(Sv.SV_ISLABEL).equals(true)) {
						tmpS = I18n.getText(getLocaleId(svr), tmpS);
						jsonDataForWork.addProperty(saveField, tmpS);
						break;
					}
					if (tmpField.getVal(Svu.SV_MUTLISELECT) != null && tmpField.getVal(Svu.SV_MUTLISELECT).equals(true)) {
						String translatedResult = Sv.EMPTY_STRING;
						StringBuilder trResBuild = new StringBuilder();

						if (tmpField.getVal(Sv.CODE_LIST_ID) != null) {
							cl = new CodeList(svr);
							HashMap<String, String> listMap = cl.getCodeList(getLocaleId(svr),
									tmpField.getAsLong(Sv.CODE_LIST_ID), true);
							String[] cArray = tmpS.split(SvConf.getMultiSelectSeparator());
							// if SvConf.getMultiSelectSeparator() is empty, set
							// default to ,
							String multiSelectOperator = SvConf.getMultiSelectSeparator() == null ? ","
									: SvConf.getMultiSelectSeparator();
							for (String tempCodeListKey : cArray) {
								translatedResult = translatedResult
										+ I18n.getText(getLocaleId(svr), listMap.get(tempCodeListKey))
										+ multiSelectOperator;
								trResBuild.append(I18n.getText(getLocaleId(svr), listMap.get(tempCodeListKey))
										+ multiSelectOperator);
							}
						}
						trResBuild.substring(0, trResBuild.length() - 1);
						translatedResult = translatedResult.substring(0, translatedResult.length() - 1);
						jsonDataForWork.addProperty(saveField, trResBuild.toString());
						break;
					}
					if (tmpS != null) {
						if (Sv.LABEL_CODE.toString().equalsIgnoreCase(nameField) && doTranslate)
							tmpS = I18n.getText(getLocaleId(svr), tmpS);

						jsonDataForWork.addProperty(saveField, tmpS);
					}
					break;
				case Svu.NUMERIC:
					Number tmpN = null;
					Long isFloat = (Long) tmpField.getVal(Sv.FIELD_SCALE);
					if (isFloat == null || isFloat == 0) {
						Long tmpL = null;
						tmpL = recordObject.getAsLong(readField);
						tmpN = tmpL;
					} else {
						Double tmpDo = null;
						tmpDo = recordObject.getAsDouble(nameField);
						tmpN = tmpDo;
					}
					if (tmpN != null)
						jsonDataForWork.addProperty(saveField, tmpN);
					break;
				case Sv.BOOLEAN:
					jsonDataForWork = addBoleanParse(jsonData, recordObject, readField, saveField);
					break;
				case Svu.DATE: // for some reason date was saved as datetime
					DateTime tmpDsh = new DateTime(recordObject.getVal(readField));

					int monthInt = tmpDsh.monthOfYear().get();
					int dayInt = tmpDsh.dayOfMonth().get();
					String monthStr = ((monthInt < 10) ? "0" : "") + String.valueOf(monthInt);
					String dayStr = ((dayInt < 10) ? "0" : "") + String.valueOf(dayInt);
					jsonDataForWork.addProperty(saveField, tmpDsh.year().get() + "-" + monthStr + "-" + dayStr);

					break;
				case Svu.TIMESTAMP:
				case Svu.DATETIME:
					DateTime tmpDlg = null;
					tmpDlg = (DateTime) recordObject.getVal(readField);
					if (tmpDlg != null)
						jsonDataForWork.addProperty(saveField, tmpDlg.toString());
					break;
				default:
				}
			} catch (Exception e) {
				debugException(e);
			} finally {
				if (cl != null)
					cl.release();
			}
		return jsonDataForWork;
	}

	public static void debugException(Exception e) {
		if (log4j.isDebugEnabled())
			log4j.debug(e.getMessage(), e);
	}

	public static Boolean processField(String fieldName) {
		Boolean retVal = false;
		if (!Sv.PKID.equalsIgnoreCase(fieldName) && !"GUI_METADATA".equalsIgnoreCase(fieldName)
				&& !"CENTROID".equalsIgnoreCase(fieldName) && !Sv.GEOM.equalsIgnoreCase(fieldName))
			retVal = true;
		return retVal;
	}

	public static JsonObject prapareSvarogData(DbDataObject dbo, String tableName, JsonObject jBo) {
		jBo.addProperty(tableName + "." + Sv.PKID, dbo.getPkid());
		jBo.addProperty(tableName + "." + Sv.OBJECT_ID, dbo.getObjectId());
		jBo.addProperty(tableName + "." + Sv.PARENT_ID, dbo.getParentId());
		jBo.addProperty(tableName + "." + Svu.OBJECT_TYPE, dbo.getObjectType());
		jBo.addProperty(tableName + "." + Sv.STATUS, dbo.getStatus());
		return jBo;
	}

	public static JsonObject prapareSvarogDataFull(DbDataObject dbo, String tableName, JsonObject jBo) {
		JsonObject jBo1 = prapareSvarogData(dbo, tableName, jBo);
		jBo1.addProperty(tableName + "." + Svu.USER_ID, dbo.getUserId());
		jBo1.addProperty(tableName + "." + Svu.DT_INSERT, dbo.getDtInsert().toString());
		jBo1.addProperty(tableName + "." + Svu.DT_DELETE, dbo.getDtDelete().toString());
		return jBo1;
	}

	public static JsonObject prapareSvarogDataFull(DbDataObject dbo, String tableName, int i, JsonObject jBo) {
		JsonObject jData = jBo;
		String tmpStr = Sv.TBL + i + "_";
		if (dbo.getVal(tmpStr + Sv.PKID) == null)
			jData = prapareSvarogDataFull(dbo, tableName, jBo);
		else {
			if (dbo.getVal(tmpStr + Sv.PKID) != null)
				jData.addProperty(tableName + "." + Sv.PKID, (Long) dbo.getVal(tmpStr + Sv.PKID));
			if (dbo.getVal(tmpStr + Svu.META_PKID) != null)
				jData.addProperty(tableName + "." + Svu.META_PKID, (Long) dbo.getVal(tmpStr + Svu.META_PKID));
			if (dbo.getVal(tmpStr + Sv.OBJECT_ID) != null)
				jData.addProperty(tableName + "." + Sv.OBJECT_ID, (Long) dbo.getVal(tmpStr + Sv.OBJECT_ID));
			if (dbo.getVal(tmpStr + Sv.PARENT_ID) != null)
				jData.addProperty(tableName + "." + Sv.PARENT_ID, (Long) dbo.getVal(tmpStr + Sv.PARENT_ID));
			if (dbo.getVal(tmpStr + Svu.OBJECT_TYPE) != null)
				jData.addProperty(tableName + "." + Svu.OBJECT_TYPE, (Long) dbo.getVal(tmpStr + Svu.OBJECT_TYPE));
			if (dbo.getVal(tmpStr + Sv.STATUS) != null)
				jData.addProperty(tableName + "." + Sv.STATUS, dbo.getAsString(tmpStr + Sv.STATUS));
			if (dbo.getVal(tmpStr + Svu.USER_ID) != null)
				jData.addProperty(tableName + "." + Svu.USER_ID, (Long) dbo.getVal(tmpStr + Svu.USER_ID));
			if (dbo.getVal(tmpStr + Svu.DT_DELETE) != null)
				jData.addProperty(tableName + "." + Svu.DT_DELETE, dbo.getAsString(tmpStr + Svu.DT_DELETE));
			if (dbo.getVal(tmpStr + Svu.DT_INSERT) != null)
				jData.addProperty(tableName + "." + Svu.DT_INSERT, dbo.getAsString(tmpStr + Svu.DT_INSERT));
		}
		return jData;
	}

	public static JsonObject prapareSvarogData(DbDataObject dbo, String tableName, int i, JsonObject jBo) {
		JsonObject jData = jBo;
		String tmpStr = Sv.TBL + i + "_";
		if (dbo.getVal(tmpStr + Sv.PKID) == null)
			jData = prapareSvarogData(dbo, tableName, jBo);
		else {
			if (dbo.getVal(tmpStr + Sv.PKID) != null)
				jData.addProperty(tableName + "." + Sv.PKID, (Long) dbo.getVal(tmpStr + Sv.PKID));
			if (dbo.getVal(tmpStr + Sv.OBJECT_ID) != null)
				jData.addProperty(tableName + "." + Sv.OBJECT_ID, (Long) dbo.getVal(tmpStr + Sv.OBJECT_ID));
			if (dbo.getVal(tmpStr + Sv.PARENT_ID) != null)
				jData.addProperty(tableName + "." + Sv.PARENT_ID, (Long) dbo.getVal(tmpStr + Sv.PARENT_ID));
			if (dbo.getVal(tmpStr + Svu.OBJECT_TYPE) != null)
				jData.addProperty(tableName + "." + Svu.OBJECT_TYPE, (Long) dbo.getVal(tmpStr + Svu.OBJECT_TYPE));
			if (dbo.getVal(tmpStr + Sv.STATUS) != null)
				jData.addProperty(tableName + "." + Sv.STATUS, dbo.getAsString(tmpStr + Sv.STATUS));
		}
		return jData;
	}

	/**
	 * procedure to generate part of the Json string for the data that is part of
	 * SVAROG core, overloaded version used when we make join query
	 * 
	 * @param vData            DbDataArray this is where all data from the query is
	 *                         stored
	 * @param tablesUsedArray  Array of String array of tables used in the query,
	 *                         that MUST be in same order as used in building the
	 *                         query
	 * @param tableShowArray   Array of Boolean , to save on some time and
	 *                         string/Json size, we can hide full tables that don't
	 *                         have anything for display
	 * @param tablesusedCount  int , when we make svarog join every table is renamed
	 *                         to TBL[i] , this will tell us how many tables are we
	 *                         joining so we don't go out of index
	 * @param doTranslate      Boolean if set to TRUE it will translate all
	 *                         label_codes, so it should be TRUE most of the time
	 *                         except in administrative console where we have to set
	 *                         the codes
	 * @param svr              connected SvReader
	 * @param isfullSvarogData Boolean if set to TRUE it will return full REPO field
	 *                         data, FALSE will return whatever is set in the REPO
	 *                         fields GUI_METADATA
	 * 
	 * @return JSON JsonArray with data from VData array
	 */
	public static JsonArray prapareTableQueryData(DbDataArray vData, String[] tablesUsedArray, Boolean[] tableShowArray,
			int tablesusedCount, Boolean doTranslate, SvReader svr, Boolean isfullSvarogData) {
		JsonArray jarr = new JsonArray();
		if (vData != null && !vData.getItems().isEmpty())
			for (int j = 0; j < vData.getItems().size(); j++) {
				jarr.add(prapareTableQueryJsonObject(vData.getItems().get(j), tablesUsedArray, tableShowArray,
						tablesusedCount, doTranslate, svr, isfullSvarogData));
			}
		return jarr;
	}

	/**
	 * procedure to generate part of the Json string for the data that is part of
	 * SVAROG core, overloaded version used when we make join query
	 * 
	 * @param vData           DbDataArray this is where all data from the query is
	 *                        stored
	 * @param tablesUsedArray Array of String array of tables used in the query,
	 *                        that MUST be in same order as used in building the
	 *                        query
	 * @param tableShowArray  Array of Boolean , to save on some time and
	 *                        string/Json size, we can hide full tables that don't
	 *                        have anything for display
	 * @param i               int , when we make svarog join every table is renamed
	 *                        to TBL[i] , this will tell us how many tables are we
	 *                        joining so we don't go out of index
	 * @param svr             connected SvReader
	 * 
	 * @return JSON String with data from VData array
	 */
	public static String prapareTableQueryData(DbDataArray vData, String[] tablesUsedArray, Boolean[] tableShowArray,
			int tablesusedCount, Boolean doTranslate, SvReader svr) {
		return prapareTableQueryData(vData, tablesUsedArray, tableShowArray, tablesusedCount, doTranslate, svr, false)
				.toString();
	}

	private static JsonObject prapareTableQueryJsonObject(DbDataObject obj1, String[] tablesUsedArray,
			Boolean[] tableShowArray, int tablesusedCount, Boolean doTranslate, SvReader svr,
			Boolean isfullSvarogData) {
		JsonObject jData = new JsonObject();
		for (int k = 0; k < tablesusedCount; k++)
			if (tableShowArray[k]) {
				DbDataObject tableObject = SvCore.getDbtByName(tablesUsedArray[k]);
				DbDataArray typetoGet = SvCore.getFields(tableObject.getObjectId());
				if (isfullSvarogData)
					jData = prapareSvarogDataFull(obj1, tablesUsedArray[k], k, jData);
				else
					jData = prapareSvarogData(obj1, tablesUsedArray[k], k, jData);
				for (int i = 0; i < typetoGet.getItems().size(); i++) {
					String tmpField = typetoGet.getItems().get(i).getAsString(Sv.FIELD_NAME);
					String fieldToRead = Sv.EMPTY_STRING;
					if (tablesusedCount != 1)
						fieldToRead = Sv.TBL + k + "_";
					fieldToRead = fieldToRead + tmpField;
					String readField = tablesUsedArray[k] + "." + tmpField;
					jData = addValueToJsonObject2(jData, obj1, typetoGet.getItems().get(i), fieldToRead, readField,
							doTranslate, svr);
				}
			}
		return jData;
	}

	public static JsonArray prapareTableQueryDataV1(List<DbDataObject> vData, String[] tablesUsedArray,
			Boolean[] tableShowArray, int tablesusedCount, Boolean doTranslate, SvReader svr,
			Boolean isfullSvarogData) {
		JsonArray jarr = new JsonArray();
		if (vData != null && !vData.isEmpty())
			for (int j = 0; j < vData.size(); j++) {
				jarr.add(prapareTableQueryJsonObject(vData.get(j), tablesUsedArray, tableShowArray, tablesusedCount,
						doTranslate, svr, isfullSvarogData));
			}
		return jarr;
	}



	/** getLinkedOrgUnitsPerUser */
	public DbDataArray getLinkedOrgUnitsPerUser(SvReader svr) throws SvException {
		// DbDataArray linkedOrgUnitsPerUser = new DbDataArray();
		DbDataObject dbLink = SvReader.getLinkType(Sv.POA, svCONST.OBJECT_TYPE_USER, svCONST.OBJECT_TYPE_ORG_UNITS);
		DbDataObject dboUser = SvReader.getUserBySession(svr.getSessionId());
		// linkedOrgUnitsPerUser =
		// svr.getObjectsByLinkedId(dboUser.getObjectId(), dbLink, null, 0, 0);
		return svr.getObjectsByLinkedId(dboUser.getObjectId(), dbLink, null, 0, 0);
	}



}
