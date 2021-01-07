package com.yhy.elasticsearch.respository;

import com.yhy.elasticsearch.dto.DirectoryTreeDTO;

import java.util.*;
import java.util.stream.Collectors;

public class DirectoryTest {


    /**
     * 获取当前目录到根节点的层级
     *
     * @param treeNode  当前节点
     * @param treeNodes 所有节点
     * @return
     */
    public static int countDirLevelToRoot(DirectoryTreeDTO treeNode, List<DirectoryTreeDTO> treeNodes) {
        Deque<DirectoryTreeDTO> stack = new LinkedList<>();
        int ans = 0;
        if (treeNode.getParentId() == 0) {
            return 1;
        }
        for (DirectoryTreeDTO node : treeNodes) {
            if (node == null) {
                continue;
            }
            stack.push(node);
        }
        while (!stack.isEmpty()) {
            DirectoryTreeDTO node = stack.pop();
            if (node.getId().equals(treeNode.getParentId())) {
                ans++;
                treeNode = node;
            }

        }
        return ans + 1;
    }

    public static int countDirLevelToRoot2(DirectoryTreeDTO treeNode, List<DirectoryTreeDTO> treeNodes) {
        int sum = 0;
        for (DirectoryTreeDTO node : treeNodes) {
            if (node.getId() == treeNode.getParentId()) {
                sum++;
                treeNode = node;
            }
        }
        return sum;
    }

    public static int countDirLevelToRoot3(DirectoryTreeDTO treeNode, List<DirectoryTreeDTO> treeNodes) {
        int sum = 0;
        while (treeNode.getParentId() != 0) {
            for (DirectoryTreeDTO node : treeNodes) {
                if (node.getId().equals(treeNode.getParentId())) {
                    sum++;
                    treeNode = node;
                }
            }
        }
        return sum + 1;
    }

    /**
     * 获取到叶子结点的层级
     *
     * @param treeNode
     * @param treeNodes
     * @return
     */
    public static int contDirLevelToLeaf(DirectoryTreeDTO treeNode, List<DirectoryTreeDTO> treeNodes) {
        int num = 0;
        Map<Long, List<DirectoryTreeDTO>> parentMap = treeNodes.stream().collect(Collectors.groupingBy(DirectoryTreeDTO::getParentId));
        while (parentMap.containsKey(treeNode.getId())) {
            for (DirectoryTreeDTO node : treeNodes) {
                if (node.getParentId().equals(treeNode.getId())) {
                    num++;
                    treeNode = node;
                }
            }
        }
        return num + 1;

    }


    public static int contDirLevelToLeaf2(List<DirectoryTreeDTO> childList, int num, Map<Long, List<DirectoryTreeDTO>> parentMap) {
        List<DirectoryTreeDTO> nodeList = new ArrayList<>();
        for (DirectoryTreeDTO node : childList) {
            List<DirectoryTreeDTO> children = Optional.ofNullable(parentMap.get(node.getId())).orElse(new ArrayList<>());
            if (!children.isEmpty()) {
                nodeList.addAll(children);
            }
        }
        if (!nodeList.isEmpty()) {
            return contDirLevelToLeaf2(nodeList, num + 1, parentMap);
        }
        return num + 1;

    }

    public static void contDirLevelToLeaf3(List<DirectoryTreeDTO> childList, int num, Map<Long, List<DirectoryTreeDTO>> parentMap) {


//        for (DirectoryTreeDTO node : nodeList) {
//            if (parentMap.containsKey(node.getId())) {
//                nodeList.addAll(parentMap.get(node.getId()));
//                num++;
//            }
//        }
//        return num + 1;
        List<DirectoryTreeDTO> nodeList = new ArrayList<>();
        for (DirectoryTreeDTO node : childList) {
            List<DirectoryTreeDTO> children = Optional.ofNullable(parentMap.get(node.getId())).orElse(new ArrayList<>());
            if (!children.isEmpty()) {
                nodeList.addAll(children);
            }
        }
        if (!nodeList.isEmpty()) {
            contDirLevelToLeaf2(nodeList, num + 1, parentMap);
        }

    }

//    //递归查询所有子菜单的方法
//    private void findSubNode(List<Integer> deleteIds, List<Integer> selectIds) {
//        List<Integer> ids = authMenuDao.getChildListByParentList(selectIds);
//        if (!ids.isEmpty()) {
//            deleteIds.addAll(ids);
//            findSubNode(deleteIds, ids);
//
//        }
//    }

    /**
     * 递归当前节点下所有子节点
     *
     * @param treeNode  当前节点
     * @param childList 子节点列表
     * @param treeNodes 所有节点
     */
    private void getChildList(DirectoryTreeDTO
                                      treeNode, List<DirectoryTreeDTO> childList, List<DirectoryTreeDTO> treeNodes) {

        for (DirectoryTreeDTO it : treeNodes) {
            if (treeNode.getId().equals(it.getParentId())) {
                childList.add(it);
                getChildList(it, childList, treeNodes);
            }
        }
    }

    public static void getChildList(List<DirectoryTreeDTO> childList, List<DirectoryTreeDTO> allNodes, Map<Long, List<DirectoryTreeDTO>> nodesMap) {
        List<DirectoryTreeDTO> nodeList=new ArrayList<>();
        for(DirectoryTreeDTO node:childList){
            List<DirectoryTreeDTO> children=Optional.ofNullable(nodesMap.get(node.getId())) .orElse(new ArrayList<>());
            if(!children.isEmpty()){
                nodeList.addAll(children);
                allNodes.addAll(children);
            }
        }
        if(!nodeList.isEmpty()){
            getChildList(nodeList,allNodes,nodesMap);
        }
    }


    public static void main(String[] args) {
        List<DirectoryTreeDTO> treeDTOS = new ArrayList<>();

        DirectoryTreeDTO d5 = new DirectoryTreeDTO();
        d5.setId(5L);
        d5.setParentId(3L);
        d5.setDirectoryName("子目录4");
        treeDTOS.add(d5);


        DirectoryTreeDTO d12 = new DirectoryTreeDTO();
        d12.setId(6L);
        d12.setParentId(2L);
        d12.setDirectoryName("子目录1子目录");
        treeDTOS.add(d12);

        DirectoryTreeDTO d23 = new DirectoryTreeDTO();
        d23.setId(7L);
        d23.setParentId(6L);
        d23.setDirectoryName("子目录1子目录A");
        treeDTOS.add(d23);

        DirectoryTreeDTO d44 = new DirectoryTreeDTO();
        d44.setId(44L);
        d44.setParentId(7L);
        d44.setDirectoryName("子目录1子目录AC");
        treeDTOS.add(d44);

        DirectoryTreeDTO d43 = new DirectoryTreeDTO();
        d43.setId(43L);
        d43.setParentId(7L);
        d43.setDirectoryName("子目录1子目录AB");
        treeDTOS.add(d43);


        DirectoryTreeDTO d3 = new DirectoryTreeDTO();
        d3.setId(3L);
        d3.setParentId(2L);
        d3.setDirectoryName("子目录2");
        treeDTOS.add(d3);


        DirectoryTreeDTO d4 = new DirectoryTreeDTO();
        d4.setId(4L);
        d4.setParentId(3L);
        d4.setDirectoryName("子目录3");
        treeDTOS.add(d4);


        DirectoryTreeDTO d41 = new DirectoryTreeDTO();
        d41.setId(41L);
        d41.setParentId(3L);
        d41.setDirectoryName("子目录2A");
        treeDTOS.add(d41);


        DirectoryTreeDTO d45 = new DirectoryTreeDTO();
        d45.setId(45L);
        d45.setParentId(43L);
        d45.setDirectoryName("子目录1子目录AD");
        treeDTOS.add(d45);

        DirectoryTreeDTO d1 = new DirectoryTreeDTO();
        d1.setId(1L);
        d1.setParentId(0L);
        d1.setDirectoryName("根目录");
        treeDTOS.add(d1);

        DirectoryTreeDTO d2 = new DirectoryTreeDTO();
        d2.setId(2L);
        d2.setParentId(1L);
        d2.setDirectoryName("子目录1");
        treeDTOS.add(d2);

//        //以父节点id为key的哈希表
//        Map<Long, List<DirectoryTreeDTO>> map = treeDTOS.stream()
//                .collect(Collectors.groupingBy(DirectoryTreeDTO::getParentId));
//
//        //hashmap按降序
//
//        //返回层序排列的list
//        List<DirectoryTreeDTO> resultList = new ArrayList<>();
//        for (Map.Entry<Long, List<DirectoryTreeDTO>> entry : map.entrySet()) {
//            List<DirectoryTreeDTO> value = entry.getValue();
//            resultList.addAll(value);
//        }
        List<DirectoryTreeDTO> sortList1 = treeDTOS.stream().filter(i -> i.getParentId() != 0).sorted(Comparator.comparing(DirectoryTreeDTO::getParentId).reversed()).collect(Collectors.toList());

        List<DirectoryTreeDTO> resultList = new ArrayList<>(sortList1);
        List<DirectoryTreeDTO> sortList2 = treeDTOS.stream().filter(i -> i.getParentId() == 0).collect(Collectors.toList());
        resultList.addAll(sortList2);

        System.out.println(countDirLevelToRoot3(d45, resultList));

        Map<Long, List<DirectoryTreeDTO>> parentMap = treeDTOS.stream().collect(Collectors.groupingBy(DirectoryTreeDTO::getParentId));
        int num = 0;
        List<DirectoryTreeDTO> nodeList = new ArrayList<>();
        nodeList.add(d23);
        contDirLevelToLeaf2(nodeList, num, parentMap);
        System.out.println(contDirLevelToLeaf2(nodeList, num, parentMap));
//        contDirLevelToLeaf3(nodeList,num,parentMap);
//        System.out.println(num);
        List<DirectoryTreeDTO> allNodes=new ArrayList<>();
        getChildList(nodeList,allNodes,parentMap);
        for(DirectoryTreeDTO it:allNodes){
            System.out.println(it);
        }
    }

}
