package org.opennms.ng.persistence.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.opennms.netmgt.model.OnmsCategory;
import org.opennms.netmgt.model.OnmsDistPoller;
import org.opennms.netmgt.model.SurveillanceStatus;
import org.opennms.ng.persistence.entities.OnmsIpInterface;
import org.opennms.ng.persistence.entities.OnmsNode;

public interface OnmsNodeDao extends GenericDao<OnmsNode,Integer> {

    /**
       * Get a node based on it's node ID or foreignSource:foreignId
       *
       * @param lookupCriteria the criteria, either the node ID, or a colon-separated string of foreignSource:foreignId
       * @return the node
       */
      OnmsNode get(String lookupCriteria);

      /**
       * Light weight call to simply get the node label without loading the entire node.
       *
       * @param id
       * @return A String representing the provisioned label for the node.  Returns null if not found.
       */
      String getLabelForId(Integer id);

      /**
       * <p>findByLabel</p>
       *
       * @param label a {@link java.lang.String} object.
       * @return a {@link java.util.Collection} object.
       */
      List<OnmsNode> findByLabel(String label);

      /**
       * <p>findNodes</p>
       *
       * @param dp a {@link OnmsDistPoller} object.
       * @return a {@link java.util.Collection} object.
       */
      List<OnmsNode> findNodes(OnmsDistPoller dp);

      /**
       * <p>getHierarchy</p>
       *
       * @param id a {@link java.lang.Integer} object.
       * @return a {@link OnmsNode} object.
       */
      OnmsNode getHierarchy(Integer id);

      /**
       * <p>getForeignIdToNodeIdMap</p>
       *
       * @param foreignSource a {@link java.lang.String} object.
       * @return a {@link java.util.Map} object.
       */
      Map<String, Integer> getForeignIdToNodeIdMap(String foreignSource);

      /**
       * <p>findAllByVarCharAssetColumn</p>
       *
       * @param columnName a {@link java.lang.String} object.
       * @param columnValue a {@link java.lang.String} object.
       * @return a {@link java.util.Collection} object.
       */
      List<OnmsNode> findAllByVarCharAssetColumn(String columnName, String columnValue);

      /**
       * <p>findAllByVarCharAssetColumnCategoryList</p>
       *
       * @param columnName a {@link java.lang.String} object.
       * @param columnValue a {@link java.lang.String} object.
       * @param categories a {@link java.util.Collection} object.
       * @return a {@link java.util.Collection} object.
       */
      List<OnmsNode> findAllByVarCharAssetColumnCategoryList(String columnName, String columnValue,
              Collection<OnmsCategory> categories);

      /**
       * <p>findByCategory</p>
       *
       * @param category a {@link OnmsCategory} object.
       * @return a {@link java.util.Collection} object.
       */
      List<OnmsNode> findByCategory(OnmsCategory category);

      /**
       * <p>findAllByCategoryList</p>
       *
       * @param categories a {@link java.util.Collection} object.
       * @return a {@link java.util.Collection} object.
       */
      List<OnmsNode> findAllByCategoryList(Collection<OnmsCategory> categories);

      /**
       * <p>findAllByCategoryLists</p>
       *
       * @param rowCatNames a {@link java.util.Collection} object.
       * @param colCatNames a {@link java.util.Collection} object.
       * @return a {@link java.util.Collection} object.
       */
      List<OnmsNode> findAllByCategoryLists(Collection<OnmsCategory> rowCatNames, Collection<OnmsCategory> colCatNames);

      /**
       * Returns a list of nodes ordered by label.
       *
       * @return a {@link java.util.List} object.
       */

      List<OnmsNode> findAll();

      /**
       * <p>findByForeignSource</p>
       *
       * @param foreignSource a {@link java.lang.String} object.
       * @return a {@link java.util.List} object.
       */
      List<OnmsNode> findByForeignSource(String foreignSource);

      /**
       * <p>findByForeignId</p>
       *
       * @param foreignSource a {@link java.lang.String} object.
       * @param foreignId a {@link java.lang.String} object.
       * @return a {@link OnmsNode} object.
       */
      OnmsNode findByForeignId(String foreignSource, String foreignId);

      /**
       * <p>getNodeCountForForeignSource</p>
       *
       * @param groupName a {@link java.lang.String} object.
       * @return a int.
       */
      int getNodeCountForForeignSource(String groupName);

      /**
       * <p>findAllProvisionedNodes</p>
       *
       * @return a {@link java.util.List} object.
       */
      List<OnmsNode> findAllProvisionedNodes();

      /**
       * <p>findObsoleteIpInterfaces</p>
       *
       * @param nodeId a {@link java.lang.Integer} object.
       * @param scanStamp a {@link java.util.Date} object.
       * @return a {@link java.util.List} object.
       */
      List<OnmsIpInterface> findObsoleteIpInterfaces(Integer nodeId, Date scanStamp);

      /**
       * <p>deleteObsoleteInterfaces</p>
       *
       * @param nodeId a {@link java.lang.Integer} object.
       * @param scanStamp a {@link java.util.Date} object.
       */
      void deleteObsoleteInterfaces(Integer nodeId, Date scanStamp);

      /**
       * <p>updateNodeScanStamp</p>
       *
       * @param nodeId a {@link java.lang.Integer} object.
       * @param scanStamp a {@link java.util.Date} object.
       */
      void updateNodeScanStamp(Integer nodeId, Date scanStamp);

      /**
       * <p>getNodeIds</p>
       *
       * @return a {@link java.util.Collection} object.
       */
      Collection<Integer> getNodeIds();

      /**
       * <p>findByForeignSourceAndIpAddress</p>
       *
       * @param foreignSource a {@link java.lang.String} object.
       * @param ipAddress a {@link java.lang.String} object.
       * @return a {@link java.util.List} object.
       */
      List<OnmsNode> findByForeignSourceAndIpAddress(String foreignSource, String ipAddress);

      SurveillanceStatus findSurveillanceStatusByCategoryLists(Collection<OnmsCategory> rowCategories, Collection<OnmsCategory> columnCategories);

      Integer getNextNodeId (Integer nodeId);

      Integer getPreviousNodeId (Integer nodeId);
}
