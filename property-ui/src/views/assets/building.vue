<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item label="楼栋名称">
          <el-input prefix-icon="el-icon-search" placeholder="请输入楼栋名称" v-model="nameText" clearable
                    style="width: 250px">
          </el-input>
        </el-form-item>
        <el-form-item label="楼型">
          <el-select v-model="typeSelect" placeholder="请选择楼型">
            <el-option label="全部" value="-1"></el-option>
            <el-option label="电梯房" value="0"></el-option>
            <el-option label="楼梯房" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="getNameAndTypeList">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-refresh" @click="reset" v-permission="'role:add'" plain>重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div style="margin-bottom: 16px">
      <el-button type="primary" icon="el-icon-circle-plus-outline" @click="showCreate" v-permission="'role:add'">
        添加</el-button>
    </div>
    <el-table :data="list" v-loading="listLoading" border :header-cell-style="{background:'#f5f7fa',color:'#606266'}">
      <el-table-column align="center" label="序号" width="50">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="name" label="楼栋" width="200"></el-table-column>
      <el-table-column align="center" prop="layer" label="层数" width="160"/>
      <el-table-column align="center" prop="area" label="面积" width="160"/>
      <el-table-column align="center" prop="type" label="类型" :formatter="typeFormat" width="160"/>
      <el-table-column align="center" prop="description" label="描述" style="width: 40px"/>
      <el-table-column align="center" prop="gmtCreate" label="创建时间" width="200"/>
      <el-table-column align="center" prop="gmtModified" label="最近修改时间" width="200"/>
      <el-table-column align="center" label="管理" width="200">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="edit" @click="showUpdate(scope.$index)"
                     v-permission="'role:update'">修改</el-button>
          <el-button type="danger" size="mini" icon="edit" @click="showRemove(scope.$index)"
                     v-permission="'role:delete'">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="listQuery.pageNum"
      :page-size="listQuery.pageRow"
      :total="totalCount"
      :page-sizes="[10, 20, 30, 50]"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="tempBuilding" label-position="left" label-width="80px"
               style='width: 100%; margin-left: 48px'>
        <el-form-item label="楼栋名称" v-show="dialogStatus !== 'remove'" required>
          <el-input type="text" v-model="tempBuilding.name" style="width: 50%;">
          </el-input>
        </el-form-item>
        <el-form-item label="层数" v-show="dialogStatus !== 'remove'" required>
          <el-input type="text" v-model="tempBuilding.layer" style="width: 30%;">
          </el-input>
        </el-form-item>
        <el-form-item label="面积" v-show="dialogStatus !== 'remove'" required>
          <el-input type="text" v-model="tempBuilding.area" style="width: 30%;">
          </el-input>
        </el-form-item>
        <el-form-item label="楼栋类型" v-show="dialogStatus !== 'remove'" required>
          <el-select v-model="tempBuilding.type" placeholder="请选择" style="width: 30%;">
            <el-option
              v-for="item in typeList"
              :key="item.typeValue"
              :label="item.typeLabel"
              :value="item.typeValue">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="楼栋描述" v-show="dialogStatus !== 'remove'" required>
          <el-input type="textarea" :rows="4" placeholder="请输入楼栋描述" v-model="tempBuilding.description"
                    style="width: 50%;" maxlength="120" show-word-limit>
          </el-input>
        </el-form-item>

        <el-form-item v-show="dialogStatus === 'remove'" required>
          <span style="font-size: 16px">确定要删除 {{tempBuilding.name}} 吗？</span>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="success" v-show="dialogStatus==='create'" @click="createBuilding">创 建</el-button>
        <el-button type="primary" v-show="dialogStatus==='update'" @click="updateBuilding">修 改</el-button>
        <el-button type="primary" v-show="dialogStatus==='remove'" @click="removeBuilding">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "building",
  data() {
    return {
      // 搜索内容数据
      nameText: '',
      // 类型选择
      typeSelect: '',
      //分页组件--数据总条数
      totalCount: 0,
      //表格的数据
      list: [],
      //数据加载等待动画
      listLoading: false,
      listQuery: {
        //页码
        pageNum: 1,
        //每页条数
        pageRow: 10,
        name: ''
      },
      dialogStatus: 'create',
      dialogFormVisible: false,
      dialogVisible: false,
      textMap: {
        create: '创建楼栋',
        update: '编辑楼栋',
        remove: '提示'
      },
      tempBuilding: {
        id: '',
        name: '',
        layer: '',
        area: '',
        type: '',
        description: '',
        gmtCreate: '',
        gmtModified: ''
      },
      typeList: [
        {
          typeValue: '0',
          typeLabel: '楼梯房'
        }, {
          typeValue: '1',
          typeLabel: '电梯房'
        }
      ]
    }
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.listLoading = true;
      this.api({
        url: "/building/listBuilding",
        method: "get",
        params: this.listQuery
      }).then(data => {
        this.listLoading = false;
        this.list = data.list;
        this.totalCount = data.totalCount;
      })
    },
    reset() {
      if (this.nameText.trim().length !== 0 || this.typeSelect.trim().length !== 0) {
        this.getList()
      }
      this.nameText = '';
      this.typeSelect = '';

    },
    handleSizeChange(val) {
      // 改变每页数量
      this.listQuery.pageRow = val
      this.handleFilter();
    },
    handleCurrentChange(val) {
      // 改变页码
      this.listQuery.pageNum = val
      this.getList();
    },
    handleFilter() {
      // 改变了查询条件,从第一页开始查询
      this.listQuery.pageNum = 1
      this.getList()
    },
    getIndex($index) {
      // 表格序号
      return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
    },
    showCreate() {
      // 显示新增对话框
      this.tempBuilding.name = "";
      this.tempBuilding.layer = "";
      this.tempBuilding.area = "";
      this.tempBuilding.type = "";
      this.tempBuilding.description = "";
      this.dialogStatus = "create"
      this.dialogFormVisible = true
    },
    showUpdate($index) {
      let building = this.list[$index];
      this.tempBuilding.id = building.id;
      this.tempBuilding.name = building.name;
      this.tempBuilding.layer = building.layer;
      this.tempBuilding.area = building.area;
      this.tempBuilding.type = building.type;
      this.tempBuilding.description = building.description;
      this.dialogStatus = "update"
      this.dialogFormVisible = true
    },
    showRemove($index) {
      let building = this.list[$index];
      this.tempBuilding.id = building.id;
      this.tempBuilding.name = building.name;
      this.dialogStatus = "remove"
      this.dialogFormVisible = true
    },
    /**
     * 显示类型
     *
     * @param building 楼栋实体
     * @returns {string} 字符串
     */
    typeFormat(building) {
      return building.type === '0' ? '楼梯房' : '电梯房';
    },
    validate(isCreate) {
      let building = this.tempBuilding
      if (isCreate && building.name.trim().length === 0) {
        this.$message.warning('请填写楼栋名称')
        return false
      }
      if (isCreate && building.layer.length === 0) {
        this.$message.warning('请填写楼栋总用户数')
        return false
      }
      console.log(building.type.trim())
      if (building.type.trim().length === 0) {
        this.$message.warning('请选择楼栋类型')
        return false
      }
      if (building.description.trim().length === 0) {
        this.$message.warning('请输入楼栋描述')
        return false
      }
      return true
    },
    queryValidate(isCreate) {
      if (isCreate && this.nameText.trim().length === 0 && this.typeSelect.trim().length === 0) {
        this.$message.warning('请输入或选择您的查询条件！')
        return false
      }
      return true
    },
    /**
     * 根据楼栋名称和类型进行模糊搜索
     */
    getNameAndTypeList() {
      if (!this.queryValidate(true)) return
      this.listLoading = true;
      this.api({
        url: "/building/likeBuilding",
        method: "get",
        params: {
          "pageNum" : this.listQuery.pageNum,
          "pageRow" : this.listQuery.pageRow,
          "name" : this.nameText.trim(),
          "type" : this.typeSelect === '-1' ? '' : this.typeSelect.trim(),
        }
      }).then(data => {
        this.listLoading = false;
        this.list = data.list;
        this.totalCount = data.totalCount;
      })
    },
    createBuilding() {
      if (!this.validate(true)) return
      // 保存新楼栋
      this.api({
        url: "/building/addBuilding",
        method: "post",
        data: this.tempBuilding
      }).then(() => {
        this.$message.success('添加成功！')
        this.getList();
        this.dialogFormVisible = false
      })
    },
    updateBuilding() {
      if (!this.validate(false)) return
      // 修改楼栋
      this.api({
        url: "/building/updateBuilding",
        method: "post",
        data: this.tempBuilding
      }).then(() => {
        this.$message.success('修改成功！')
        this.getList();
        this.dialogFormVisible = false
      });
    },
    removeBuilding() {
      // 删除楼栋
      this.api({
        url: "/building/deleteBuilding",
        method: "post",
        params: {
          "id" : this.tempBuilding.id
        }
      }).then(() => {
        this.$message.success('删除成功！')
        this.getList();
        this.dialogFormVisible = false
      })
    },
  }
}
</script>

<style scoped>

.el-pagination {
  float: right;
  margin-top: 16px;
}
</style>
