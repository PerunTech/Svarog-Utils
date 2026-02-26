package com.prtech.svarog.utils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.prtech.svarog.I18n;
import com.prtech.svarog.Sv;
import com.prtech.svarog.SvCore;
import com.prtech.svarog.SvException;
import com.prtech.svarog.SvReader;
import com.prtech.svarog.svCONST;
import com.prtech.svarog_common.DbDataArray;
import com.prtech.svarog_common.DbDataObject;
import com.prtech.svarog_common.DbQueryExpression;
import com.prtech.svarog_common.DbQueryObject;
import com.prtech.svarog_common.DbQueryObject.DbJoinType;
import com.prtech.svarog_common.DbQueryObject.LinkType;
import com.prtech.svarog_common.DbSearchCriterion;
import com.prtech.svarog_common.DbSearchCriterion.DbCompareOperand;
import com.prtech.svarog_common.DbSearchExpression;

public class DbReader {

	static final Logger log4j = LogManager.getLogger(DbReader.class.getName());

	public static boolean checkUniqueData(String tableName, String fieldName, Object data, SvReader svr)
			throws SvException {
		boolean isUnique = true;
		Long typeId = SvReader.getTypeIdByName(tableName);
		try {
			DbSearchCriterion dbc1 = new DbSearchCriterion(fieldName, DbCompareOperand.EQUAL, data);
			DbSearchExpression dbe = new DbSearchExpression().addDbSearchItem(dbc1);
			DbDataArray dba = svr.getObjects(dbe, typeId, null, 0, 0);

			if (dba.getItems().size() > 0) {
				isUnique = false;
			}

		} catch (SvException e) {
			e.printStackTrace();
			isUnique = false;
		}
		return isUnique;
	}

	public boolean checkIfFieldExists(String tableName, String fieldName, SvReader svr) throws SvException {
		boolean fieldExists = false;
		DbDataArray dboFieldsPerTable = SvCore.getFields(SvCore.getTypeIdByName(tableName));
		for (DbDataObject field : dboFieldsPerTable.getItems()) {
			if (field.getVal(Sv.FIELD_NAME).toString().equals(fieldName)) {
				fieldExists = true;
				break;
			}
		}
		return fieldExists;
	}

	public static boolean isFutureDate(Date firstDate) {
		boolean result = true;
		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		if (firstDate.compareTo(date) > 0) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * This method is a general method, for searching and getting back the object
	 * from the database, with or without using cache
	 * 
	 * @param objectId  - objectId of the object we are looking for
	 * @param tableName - name of the table in order to fetch the appropriate object
	 *                  type
	 * @param useCache  - flag if we want to use use the cache or not
	 * @param svr       - SvReader instance
	 * @return DbDaataObject
	 * @throws SvException
	 */
	public static DbDataObject getObjectVersion(Long objectId, String tableName, Boolean useCache, SvReader svr)
			throws SvException {
		DbDataObject obj = null;
		if (useCache) {
			obj = svr.getObjectById(objectId, SvReader.getTypeIdByName(tableName), null);
		} else {
			obj = svr.getObjectById(objectId, SvReader.getTypeIdByName(tableName), new DateTime());
		}

		return obj;
	}

	/**
	 * Method for searching objects in table (with given objectTypeId) which
	 * contains given value in given column
	 * 
	 * @param objType
	 * @param columnToSearch
	 * @param valueToSearch
	 * @return DbDataArray
	 */
	public DbDataArray searchForObjects(long objType, String columnToSearch, String valueToSearch, SvReader svReader)
			throws SvException {
		DbDataArray dba = null;

		DbSearchExpression expr = new DbSearchExpression();
		expr.addDbSearchItem(new DbSearchCriterion(columnToSearch, DbCompareOperand.EQUAL, valueToSearch));

		dba = svReader.getObjects(expr, objType, null, 0, 0);
		return dba;
	}

	public DbDataArray findFieldList(String table_name, String type, String additional, SvReader svr)
			throws SvException {
		DbDataObject tableObject = SvCore.getDbtByName(table_name);
		DbDataArray typetoGet = svr.getObjectsByParentId(tableObject.getObjectId(), svCONST.OBJECT_TYPE_FIELD, null, 0,
				0, Sv.SORT_ORDER);
		ArrayList<String> ignoreFields = getIgnoredFields(table_name, type, additional);
		if (!ignoreFields.isEmpty()) {
			typetoGet = removeFieldsFromArray(typetoGet, ignoreFields, svr);
		}
		return typetoGet;
	}

	private ArrayList<String> getIgnoredFields(String table_name, String type, String additional) {
		ArrayList<String> ignoreFields = new ArrayList<>();
		switch (table_name) {
		default:
			break;
		}
		return ignoreFields;
	}

	private DbDataArray removeFieldsFromArray(DbDataArray fields, ArrayList<String> ignoreFields, SvReader svr) {
		DbDataArray filteredFields = new DbDataArray();
		for (DbDataObject field : fields.getItems()) {
			String fieldName = field.getVal(Sv.FIELD_NAME).toString();
			if (!ignoreFields.contains(fieldName)) {
				filteredFields.addDataItem(field);
			}
		}
		return filteredFields;
	}

	protected DbDataArray searchForDependentDbDataObjects(String parentCode, String codeListName, SvReader svr)
			throws SvException {
		DbDataArray itemsFound = null;
		DbSearchExpression srchExpr = new DbSearchExpression();
		DbSearchCriterion filterByParentCodeValue = new DbSearchCriterion(Sv.PARENT_CODE_VALUE, DbCompareOperand.EQUAL,
				codeListName);
		DbSearchCriterion filterByCodeValue = new DbSearchCriterion(Sv.CODE_VALUE, DbCompareOperand.LIKE,
				parentCode + Svu.PERCENT_OPERATOR);
		srchExpr.addDbSearchItem(filterByParentCodeValue).addDbSearchItem(filterByCodeValue);

		DbDataArray searchResult = svr.getObjects(srchExpr, svCONST.OBJECT_TYPE_CODE, null, 0, 0);
		if (!searchResult.getItems().isEmpty()) {
			itemsFound = searchResult;
			String userLocale = svr.getUserLocaleId(svr.getInstanceUser());
			for (DbDataObject tempCodeItemFound : itemsFound.getItems()) {
				String translatedCodeItem = I18n.getText(userLocale,
						tempCodeItemFound.getVal(Sv.LABEL_CODE).toString());
				tempCodeItemFound.setVal(Svu.LBL_TRANSL, translatedCodeItem);
			}
		}
		return itemsFound;
	}

	/**
	 * Find the link for given objects ids and link name Object1 must be Entity or
	 * Village Object2 must be Disaster Claim
	 * 
	 * @param linkObjId1 entity/village object id
	 * @param linkObjId2 disaster claim object id
	 * @param linkName   name of the link between the two objects
	 * @param SvReader
	 * @return DbDataObject
	 */
	public static DbDataObject findLink(Long linkObjId1, Long linkObjId2, String linkName, SvReader svr)
			throws SvException {
		DbDataObject result = null;
		DbSearchExpression expr = new DbSearchExpression();
		DbDataObject dbtLink = SvReader.getDbt(svCONST.OBJECT_TYPE_LINK);
		if (linkName != null && linkName.trim().length() > 2) {
			long linkTypeId = findLinkTypeId(linkName, svr);
			if (linkObjId1 != 0L && linkObjId2 != 0L && linkTypeId != 0L) {
				DbSearchCriterion dbc1 = new DbSearchCriterion(Svu.LINK_OBJ_ID_1, DbCompareOperand.EQUAL, linkObjId1);
				DbSearchCriterion dbc2 = new DbSearchCriterion(Svu.LINK_OBJ_ID_2, DbCompareOperand.EQUAL, linkObjId2);
				DbSearchCriterion dbc3 = new DbSearchCriterion(Svu.LINK_TYPE_ID, DbCompareOperand.EQUAL, linkTypeId);
				expr.addDbSearchItem(dbc1).addDbSearchItem(dbc2).addDbSearchItem(dbc3);
				DbQueryObject dqo = new DbQueryObject(dbtLink, expr, null, null);
				DbDataArray objects = svr.getObjects(dqo, 0, 0);
				if (objects.size() == 1) {
					result = objects.get(0);
				}
			}
		}
		return result;
	}

	/**
	 * Find 'link_type' for given linkName
	 * 
	 * @param linkName name of the link
	 * @param SvReader
	 * @return String
	 */
	private static long findLinkTypeId(String linkName, SvReader svr) throws SvException {
		Long result = 0L;
		if (linkName != null && linkName.trim().length() > 2) {
			DbSearchExpression expr = new DbSearchExpression();
			DbDataObject dbtLinkType = SvReader.getDbt(svCONST.OBJECT_TYPE_LINK_TYPE);
			DbSearchCriterion dbc = new DbSearchCriterion(Svu.LINK_TYPE, DbCompareOperand.EQUAL, linkName);
			expr.addDbSearchItem(dbc);
			DbQueryObject dqo = new DbQueryObject(dbtLinkType, expr, null, null);
			DbDataArray objects = svr.getObjects(dqo, 0, 0);
			if (objects.size() == 1) {
				result = objects.get(0).getObjectId();
			}
		}
		return result;
	}

	public String findLabelNameByCode(String code, String codelistName, SvReader svr) throws SvException {
		String labelCode = Sv.EMPTY_STRING;
		String assetName = Sv.EMPTY_STRING;

		DbSearchExpression srchExpr = new DbSearchExpression();
		DbSearchCriterion filterByParentCodeValue = new DbSearchCriterion(Sv.PARENT_CODE_VALUE, DbCompareOperand.EQUAL,
				codelistName);
		DbSearchCriterion filterByCodeValue = new DbSearchCriterion(Sv.CODE_VALUE, DbCompareOperand.LIKE, code);
		srchExpr.addDbSearchItem(filterByParentCodeValue).addDbSearchItem(filterByCodeValue);

		DbDataArray searchResult = svr.getObjects(srchExpr, svCONST.OBJECT_TYPE_CODE, null, 0, 0);
		DbDataObject dbo = null;
		if (searchResult.size() == 1) {
			dbo = searchResult.get(0);
			labelCode = dbo.getVal(Sv.LABEL_CODE).toString();

			DbSearchExpression srchExpr2 = new DbSearchExpression();
			DbSearchCriterion filterByLabelCode = new DbSearchCriterion(Sv.LABEL_CODE.toString(), DbCompareOperand.LIKE,
					labelCode);
			srchExpr2.addDbSearchItem(filterByLabelCode);

			DbDataArray searchResult2 = svr.getObjects(srchExpr2, svCONST.OBJECT_TYPE_LABEL, null, 0, 0);
			if (searchResult2.size() == 1) {
				DbDataObject dbo2 = searchResult2.get(0);
				assetName = dbo2.getVal(Sv.LABEL_TEXT).toString();
			}
		}

		return assetName;
	}

	public DbDataArray getObjectsByDenotmField(String searchTableName, String searchDenormFieldName, Long objectId,
			SvReader svr) throws SvException {
		DbDataArray dba = null;

		DbSearchExpression srchExpr = new DbSearchExpression();
		DbSearchCriterion filter = new DbSearchCriterion(searchDenormFieldName, DbCompareOperand.EQUAL, objectId);

		srchExpr.addDbSearchItem(filter);
		dba = svr.getObjects(srchExpr, SvReader.getTypeIdByName(searchTableName), null, 0, 0);

		return dba;
	}

	public DbDataObject getPermissionPerUserOrUserGroup(DbDataObject dboUserGroup, String permissionCode, SvReader svr)
			throws SvException {
		DbDataObject dboAcl = null;
		DbDataObject dbtAcl = SvCore.getDbt(svCONST.OBJECT_TYPE_ACL);
		DbDataObject dbtSidAcl = SvCore.getDbt(svCONST.OBJECT_TYPE_SID_ACL);
		DbDataObject dbtSvarogUserOrUserGroup = SvCore.getDbt(svCONST.OBJECT_TYPE_GROUP);
		DbSearchCriterion dsc1 = new DbSearchCriterion(Sv.LABEL_CODE.toString(), DbCompareOperand.EQUAL, permissionCode);
		DbSearchCriterion dsc2 = new DbSearchCriterion(Sv.GROUP_NAME, DbCompareOperand.EQUAL,
				dboUserGroup.getVal(Sv.GROUP_NAME));
		DbQueryObject dqoAcl = new DbQueryObject(dbtAcl, dsc1, DbJoinType.INNER, null, LinkType.CUSTOM_FREETEXT, null,
				null);
		dqoAcl.setCustomFreeTextJoin(" on tbl0.OBJECT_ID = tbl1.ACL_OBJECT_ID ");
		DbQueryObject dqoSidAcl = new DbQueryObject(dbtSidAcl, null, DbJoinType.INNER, null, LinkType.CUSTOM_FREETEXT,
				null, null);
		dqoSidAcl.setCustomFreeTextJoin(" on tbl2.OBJECT_ID = tbl1.SID_OBJECT_ID ");
		DbQueryObject dqoSvarogUserOrUserGroup = new DbQueryObject(dbtSvarogUserOrUserGroup, dsc2, null, null);
		dqoAcl.setIsReturnType(true);
		DbQueryExpression dqe = new DbQueryExpression();
		dqe.addItem(dqoAcl);
		dqe.addItem(dqoSidAcl);
		dqe.addItem(dqoSvarogUserOrUserGroup);
		DbDataArray result = svr.getObjects(dqe, null, null);
		if (!result.isEmpty()) {
			dboAcl = result.get(0);
		}
		return dboAcl;
	}

	/**
	 * Simple help method for fetching DB object by single filter
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
		try {
			DbSearchCriterion cr1 = new DbSearchCriterion(columnName, operand, value);
			DbDataArray arrFoundDbObjects = svr.getObjects(cr1, objectType, null, 1, 0);
			if (!arrFoundDbObjects.isEmpty()) {
				dbo = arrFoundDbObjects.get(0);
			}
		} catch (SvException e) {
			log4j.error(e);
		}
		return dbo;
	}

	public boolean checkIfUserHasAdmGroup(DbDataObject dboUser, SvReader svr) throws SvException {
		boolean result = false;
		DbDataArray dba = svr.getUserGroups();
		ArrayList<DbDataObject> convertedAllGroups = dba.getItems();
		if (!convertedAllGroups.isEmpty()) {
			for (DbDataObject tempDefaultGroup : convertedAllGroups) {
				if ((tempDefaultGroup.getVal(Svu.GROUP_TYPE) != null
						&& tempDefaultGroup.getVal(Svu.GROUP_TYPE).toString().equals(Svu.ADMINISTRATORS))
						|| (tempDefaultGroup.getVal(Sv.GROUP_NAME) != null
								&& tempDefaultGroup.getVal(Sv.GROUP_NAME).toString().equals(Svu.ADMINISTRATORS))) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	private String findUserFullName(DbDataObject dbo, SvReader svr) throws SvException {
		String fullName = Sv.EMPTY_STRING;
		DbDataObject user = svr.getObjectById(dbo.getUserId(), svCONST.OBJECT_TYPE_USER, null);
		if (!Objects.isNull(user)) {
			fullName = user.getVal("FIRST_NAME").toString() + " " + user.getVal("LAST_NAME").toString();
		}
		return fullName;
	}

	public Long findCodelistObjectId(String parentLabelValue, SvReader svr) throws SvException {
		Long id = 0L;
		DbDataArray dba = null;
		DbSearchExpression srchExpr = new DbSearchExpression();

		DbSearchCriterion filterByCodeValue = new DbSearchCriterion(Sv.CODE_VALUE, DbCompareOperand.LIKE,
				parentLabelValue);
		DbSearchCriterion filterByParentCodeValue = new DbSearchCriterion(Sv.PARENT_CODE_VALUE,
				DbCompareOperand.ISNULL);

		srchExpr.addDbSearchItem(filterByCodeValue).addDbSearchItem(filterByParentCodeValue);
		dba = svr.getObjects(srchExpr, svCONST.OBJECT_TYPE_CODE, null, 0, 0);

		if (dba.size() == 1) {
			id = dba.get(0).getObjectId();
		} else {
			throw (new SvException("system.error.codelist_not_found", svCONST.systemUser, null, this));
		}

		return id;
	}

}
