package com.addzero.web.ui.hooks.tree

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// 树形选择控件的视图模型
class TreeSelectViewModel<T>(
    private val initialNodes: List<TreeNode<T>>,
    private val isMultiSelect: Boolean = false,
    private val initialExpandedNodes: Set<String> = emptySet()
) {
    private val _expandedNodes = mutableStateOf(initialExpandedNodes)
    val expandedNodes: State<Set<String>> = _expandedNodes

    internal val _selectedNodes = mutableStateOf(setOf<String>())
    val selectedNodes: State<Set<String>> = _selectedNodes

    private val _indeterminateNodes = mutableStateOf(setOf<String>())
    val indeterminateNodes: State<Set<String>> = _indeterminateNodes

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _filteredNodes = mutableStateOf(initialNodes)
    val filteredNodes: State<List<TreeNode<T>>> = _filteredNodes

    private val _isEditMode = mutableStateOf(false)
    val isEditMode: State<Boolean> = _isEditMode

    // 更新搜索文本并过滤节点
    fun updateSearchText(text: String) {
        _searchText.value = text.lowercase()
        // 如果搜索文本为空，显示所有节点
        if (text.isEmpty()) {
            _filteredNodes.value = initialNodes
            return
        }
        // 过滤节点
        _filteredNodes.value = initialNodes.mapNotNull { node ->
            filterNode(node, text.lowercase())
        }
    }

    // 获取节点状态
    private fun getNodeState(node: TreeNode<T>): NodeState {
        if (node.children.isEmpty()) {
            return LeafNodeState()
        }

        val isSelected = _selectedNodes.value.contains(node.id)
        val isIndeterminate = _indeterminateNodes.value.contains(node.id)

        return when {
            isSelected -> NonLeafSelectedState()
            isIndeterminate -> NonLeafIndeterminateState()
            else -> NonLeafUnselectedState()
        }
    }

    // 更新父节点的选中状态
    private fun updateParentNodeStates(nodeId: String) {
        fun findParentNode(
            nodes: List<TreeNode<T>>, targetId: String, parentPath: List<TreeNode<T>> = emptyList()
        ): List<TreeNode<T>>? {
            for (node in nodes) {
                if (node.children.any { it.id == targetId }) {
                    return parentPath + node
                }
                val result = findParentNode(node.children, targetId, parentPath + node)
                if (result != null) {
                    return result
                }
            }
            return null
        }

        val parentPath = findParentNode(initialNodes, nodeId)
        if (parentPath != null) {
            parentPath.forEach { parent ->
                val childrenIds = parent.children.map { it.id }.toSet()
                val selectedChildren = childrenIds.intersect(_selectedNodes.value)
                val indeterminateChildren = childrenIds.intersect(_indeterminateNodes.value)

                when {
                    selectedChildren.size == childrenIds.size -> {
                        _selectedNodes.value = _selectedNodes.value + parent.id
                        _indeterminateNodes.value = _indeterminateNodes.value - parent.id
                    }

                    selectedChildren.isEmpty() && indeterminateChildren.isEmpty() -> {
                        _selectedNodes.value = _selectedNodes.value - parent.id
                        _indeterminateNodes.value = _indeterminateNodes.value - parent.id
                    }

                    else -> {
                        _selectedNodes.value = _selectedNodes.value - parent.id
                        _indeterminateNodes.value = _indeterminateNodes.value + parent.id
                    }
                }
            }
        }
    }

    // 选择节点
    fun toggleSelection(nodeId: String) {
        val node = findNode(nodeId, initialNodes) ?: return
        val state = getNodeState(node)
        state.toggleSelection(node, this)
        updateParentNodeStates(nodeId)
    }

    // 切换节点展开/折叠状态
    fun toggleNode(nodeId: String) {
        _expandedNodes.value = if (_expandedNodes.value.contains(nodeId)) {
            _expandedNodes.value - nodeId
        } else {
            _expandedNodes.value + nodeId
        }
    }

    // 查找节点
    private fun findNode(nodeId: String, nodes: List<TreeNode<T>>): TreeNode<T>? {
        for (node in nodes) {
            if (node.id == nodeId) {
                return node
            }
            val found = findNode(nodeId, node.children)
            if (found != null) {
                return found
            }
        }
        return null
    }

    // 查找节点的所有兄弟节点ID（包括节点自身）
private fun findSiblingIds(nodeId: String): Set<String> {
    fun findParentAndSiblings(nodes: List<TreeNode<T>>, targetId: String): Set<String> {
        for (node in nodes) {
            // 在当前节点的子节点中查找目标节点
            val targetInChildren = node.children.find { it.id == targetId }
            if (targetInChildren != null) {
                // 找到目标节点，返回其所有兄弟节点的ID（包括自身）
                return node.children.map { it.id }.toSet()
            }
            // 递归查找
            val result = findParentAndSiblings(node.children, targetId)
            if (result.isNotEmpty()) {
                return result
            }
        }
        return emptySet()
    }

    return findParentAndSiblings(initialNodes, nodeId)
}

// 递归展开父节点
private fun expandParentNodes(node: TreeNode<T>, expandedNodes: MutableSet<String>) {
    // 展开当前节点
    expandedNodes.add(node.id)
    // 递归展开所有包含匹配子节点的父节点
    node.children.forEach { child ->
        expandParentNodes(child, expandedNodes)
    }
}

// 递归过滤节点
private fun filterNode(node: TreeNode<T>, searchText: String): TreeNode<T>? {
    if (node.label.lowercase().contains(searchText)) {
        return node
    }

    val filteredChildren = node.children.mapNotNull { child ->
        filterNode(child, searchText)
    }

    return if (filteredChildren.isNotEmpty()) {
        node.copy(children = filteredChildren)
    } else {
        null
    }
}
}

// 渲染节点内容
@Composable
private fun <T> renderNodeContent(node: TreeNode<T>) {
    when (node.type) {
        NodeTypes.FOLDER -> {
            Text(
                text = "📁 ${node.label}", modifier = Modifier.padding(start = 8.dp)
            )
        }
        NodeTypes.FILE -> {
            Text(
                text = "📄 ${node.label}", modifier = Modifier.padding(start = 8.dp)
            )
        }
        NodeTypes.DOCUMENT -> {
            Text(
                text = "📝 ${node.label}", modifier = Modifier.padding(start = 8.dp)
            )
        }
        NodeTypes.IMAGE -> {
            Text(
                text = "🖼️ ${node.label}", modifier = Modifier.padding(start = 8.dp)
            )
        }
        NodeTypes.VIDEO -> {
            Text(
                text = "🎬 ${node.label}", modifier = Modifier.padding(start = 8.dp)
            )
        }
        NodeTypes.AUDIO -> {
            Text(
                text = "🎵 ${node.label}", modifier = Modifier.padding(start = 8.dp)
            )
        }
        NodeTypes.LINK -> {
            Text(
                text = "🔗 ${node.label}", modifier = Modifier.padding(start = 8.dp)
            )
        }
        NodeTypes.TAG -> {
            Text(
                text = "🏷️ ${node.label}", modifier = Modifier.padding(start = 8.dp)
            )
        }
        NodeTypes.TASK -> {
            Text(
                text = "✅ ${node.label}", modifier = Modifier.padding(start = 8.dp)
            )
        }
        NodeTypes.NOTE -> {
            Text(
                text = "📔 ${node.label}", modifier = Modifier.padding(start = 8.dp)
            )
        }
        else -> {
            Text(
                text = node.label, modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

// 树形选择控件的UI组件
@Composable
fun <T> TreeSelect(
    viewModel: TreeSelectViewModel<T>, modifier: Modifier = Modifier, onNodeSelected: (Set<String>) -> Unit = {}
) {
    val expandedNodes by viewModel.expandedNodes
    val selectedNodes by viewModel.selectedNodes
    val searchText by viewModel.searchText.collectAsState()
    val filteredNodes by viewModel.filteredNodes

    Column(modifier = modifier.fillMaxWidth()) {
        // 搜索框
        OutlinedTextField(
            value = searchText,
            onValueChange = { viewModel.updateSearchText(it) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            placeholder = { Text("搜索...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "搜索") },
            singleLine = true
        )

        // 树形结构
        LazyColumn {
            items(filteredNodes) { node ->
                TreeNodeItem(
                    node = node,
                    expanded = expandedNodes.contains(node.id),
                    selected = selectedNodes.contains(node.id),
                    onToggleExpand = { viewModel.toggleNode(node.id) },
                    onSelect = {
                        viewModel.toggleSelection(node.id)
                        onNodeSelected(selectedNodes)
                    },
                    viewModel = viewModel
                )
            }
        }
    }
}

// 树节点项组件
@Composable
private fun <T> TreeNodeItem(
    node: TreeNode<T>,
    expanded: Boolean,
    selected: Boolean,
    onToggleExpand: () -> Unit,
    onSelect: () -> Unit,
    viewModel: TreeSelectViewModel<T>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = (node.level * 16).dp).padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (node.children.isNotEmpty()) {
                IconButton(onClick = onToggleExpand) {
                    Icon(
                        if (expanded) Icons.Default.ArrowDropDown else Icons.AutoMirrored.Filled.ArrowRight,
                        contentDescription = if (expanded) "折叠" else "展开"
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(40.dp))
            }

            TriStateCheckbox(
                state = when {
                    selected -> ToggleableState.On
                    viewModel.indeterminateNodes.value.contains(node.id) -> ToggleableState.Indeterminate
                    else -> ToggleableState.Off
                }, onClick = onSelect
            )

            // 根据节点类型和自定义渲染函数来渲染内容
            if (node.customRender != null) {
                node.customRender.invoke(node)
            } else {
                renderNodeContent(node)
            }
        }

        if (expanded && node.children.isNotEmpty()) {
            node.children.forEach { childNode ->
                TreeNodeItem(
                    node = childNode.copy(level = node.level + 1),
                    expanded = viewModel.expandedNodes.value.contains(childNode.id),
                    selected = viewModel.selectedNodes.value.contains(childNode.id),
                    onToggleExpand = { viewModel.toggleNode(childNode.id) },
                    onSelect = { viewModel.toggleSelection(childNode.id) },
                    viewModel = viewModel
                )
            }
        }
    }
}

// 节点状态接口
sealed interface NodeState {
    fun toggleSelection(node: TreeNode<*>, viewModel: TreeSelectViewModel<*>)
    fun toggleExpand(node: TreeNode<*>, viewModel: TreeSelectViewModel<*>)
}

// 叶子节点状态
class LeafNodeState : NodeState {
    override fun toggleSelection(node: TreeNode<*>, viewModel: TreeSelectViewModel<*>) {
        // 直接修改选中状态，而不是调用toggleSelection
        val currentSelectedNodes = viewModel._selectedNodes.value
        viewModel._selectedNodes.value = if (currentSelectedNodes.contains(node.id)) {
            currentSelectedNodes - node.id
        } else {
            currentSelectedNodes + node.id
        }
    }

    override fun toggleExpand(node: TreeNode<*>, viewModel: TreeSelectViewModel<*>) {
        // 叶子节点不支持展开/折叠操作
    }
}

// 非叶子节点未选中状态
class NonLeafUnselectedState : NodeState {
    override fun toggleSelection(node: TreeNode<*>, viewModel: TreeSelectViewModel<*>) {
        // 选中当前节点及其所有子节点
        val currentSelectedNodes = viewModel._selectedNodes.value
        viewModel._selectedNodes.value = currentSelectedNodes + node.id
        
        // 直接选中所有子节点，避免递归调用
        val childrenToSelect = mutableSetOf<String>()
        collectAllChildrenIds(node, childrenToSelect)
        viewModel._selectedNodes.value = viewModel._selectedNodes.value + childrenToSelect
    }
    
    // 收集所有子节点ID
    private fun collectAllChildrenIds(node: TreeNode<*>, result: MutableSet<String>) {
        node.children.forEach { child ->
            result.add(child.id)
            collectAllChildrenIds(child, result)
        }
    }

    override fun toggleExpand(node: TreeNode<*>, viewModel: TreeSelectViewModel<*>) {
        viewModel.toggleNode(node.id)
    }
}

// 非叶子节点半选中状态
class NonLeafIndeterminateState : NodeState {
    override fun toggleSelection(node: TreeNode<*>, viewModel: TreeSelectViewModel<*>) {
        // 全选当前节点及其所有子节点
        val currentSelectedNodes = viewModel._selectedNodes.value
        viewModel._selectedNodes.value = currentSelectedNodes + node.id
        
        // 直接选中所有子节点，避免递归调用
        val childrenToSelect = mutableSetOf<String>()
        collectAllChildrenIds(node, childrenToSelect)
        viewModel._selectedNodes.value = viewModel._selectedNodes.value + childrenToSelect
    }
    
    // 收集所有子节点ID
    private fun collectAllChildrenIds(node: TreeNode<*>, result: MutableSet<String>) {
        node.children.forEach { child ->
            result.add(child.id)
            collectAllChildrenIds(child, result)
        }
    }

    override fun toggleExpand(node: TreeNode<*>, viewModel: TreeSelectViewModel<*>) {
        viewModel.toggleNode(node.id)
    }
}

// 非叶子节点全选状态
class NonLeafSelectedState : NodeState {
    override fun toggleSelection(node: TreeNode<*>, viewModel: TreeSelectViewModel<*>) {
        // 取消选中当前节点及其所有子节点
        val currentSelectedNodes = viewModel._selectedNodes.value
        viewModel._selectedNodes.value = currentSelectedNodes - node.id
        
        // 直接取消选中所有子节点，避免递归调用
        val childrenToDeselect = mutableSetOf<String>()
        collectAllChildrenIds(node, childrenToDeselect)
        viewModel._selectedNodes.value = viewModel._selectedNodes.value - childrenToDeselect
    }
    
    // 收集所有子节点ID
    private fun collectAllChildrenIds(node: TreeNode<*>, result: MutableSet<String>) {
        node.children.forEach { child ->
            result.add(child.id)
            collectAllChildrenIds(child, result)
        }
    }

    override fun toggleExpand(node: TreeNode<*>, viewModel: TreeSelectViewModel<*>) {
        viewModel.toggleNode(node.id)
    }
}