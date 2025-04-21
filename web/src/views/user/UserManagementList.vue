<template>
  <div class="user-list">
    <h2 class="page-title">{{ $t('pageTitle.userManagementListPageTitle') }}</h2>
    <div class="operation-bar">
      <el-input v-model="searchQuery" :placeholder="$t('placeholder.searchUserByNumber')" class="search-input" clearable
        @input="handleSearch">
        <template #prefix>
          <el-icon>
            <Search />
          </el-icon>
        </template>
      </el-input>
      <el-button type="primary" @click="handleAddUser">{{ $t('button.addUser') }}</el-button>
      <el-button type="danger" :disabled="!selectedUsers.length" @click="handleBatchDelete">{{
      $t('button.batchDeleteUser')
    }}</el-button>
    </div>
    <el-table v-loading="loading" :data="users" @selection-change="handleSelectionChange" style="width: 100%">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" :label="$t('label.userId')" width="120" />
      <el-table-column prop="username" :label="$t('label.username')" width="120" />
      <el-table-column prop="nickname" :label="$t('label.nickname')" width="120" />
      <el-table-column prop="phone" :label="$t('label.phone')" width="150" />
      <el-table-column prop="role" :label="$t('label.role')" width="120">
        <template #default="{ row }">
          {{ formatRole(row.role) }}
        </template>
      </el-table-column>

      <el-table-column prop="email" :label="$t('label.email')" width="300" />
      <el-table-column prop="userCreateTime" :label="$t('label.userCreateTime')" width="250">
        <template #default="{ row }">
          {{ formatDateTime(row.userCreateTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="userLastLoginTime" :label="$t('label.userLastLoginTime')" width="250">
        <template #default="{ row }">
          {{ formatDateTime(row.userLastLoginTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" :label="$t('label.status')" width="100">
        <template #default="{ row }">
          <status-tag :status="row.status" />
        </template>
      </el-table-column>
      <el-table-column :label="$t('label.operations')" width="200">
        <template #default="{ row }">
          <el-button-group class="operation-buttons">
            <el-button link type="primary" @click="handleEdit(row)">
              <el-icon>
                <Edit />
              </el-icon>
              {{ $t('button.edit') }}
            </el-button>
            <el-button link type="danger" @click="handleDelete(row)">
              <el-icon>
                <Delete />
              </el-icon>
              {{ $t('button.delete') }}
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :page-sizes="[15, 25, 50, 100]"
        :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange"
        layout="total, sizes, prev, pager, next, jumper" />
    </div>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" append-to-body :modal-append-to-body="true"
      :lock-scroll="false" :close-on-click-modal="false" class="user-dialog">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="$t('label.id')" v-if="form.id">
          <span>{{ form.id }}</span>
        </el-form-item>
        <el-form-item :label="$t('label.username')" prop="username">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item :label="$t('label.nickname')" prop="nickname">
          <el-input v-model="form.nickname" />
        </el-form-item>
        <el-form-item :label="$t('label.email')" prop="email">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item :label="$t('label.phone')" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item :label="$t('label.role')" prop="role">
          <el-select v-model="form.role">
            <el-option :label="$t('label.admin')" value="ADMIN" />
            <el-option :label="$t('label.doctor')" value="DOCTOR" />
            <el-option :label="$t('label.patient')" value="PATIENT" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('label.status')" prop="status">
          <el-select v-model="form.status">
            <el-option :label="'ACTIVE'" value="ACTIVE" />
            <el-option :label="'INACTIVE'" value="INACTIVE" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">{{ $t('button.cancel') }}</el-button>
        <el-button type="primary" @click="handleSubmit">{{ $t('button.submit') }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Edit, Delete } from '@element-plus/icons-vue'
import { userRules } from '@/assets/utils/validation'
import { getAllUsers, createUser, updateUser, deleteUser } from '@/api/user'
import { formatDateTime, formatRole } from '@/utils/layoutFormat'
import StatusTag from '@/components/layout/StatusTag.vue'

const loading = ref(false)
const users = ref([])
const selectedUsers = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref()
const searchQuery = ref('')

// 分页相关
const currentPage = ref(1)
const pageSize = ref(15)
const total = ref(0)

const form = ref({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  user_create_time: '',
  role: 'user',
  status: 'active'
})

const rules = userRules

const fetchUsers = async () => {
  loading.value = true
  try {
    const response = await getAllUsers(currentPage.value - 1, pageSize.value, searchQuery.value)
    users.value = response.content
    total.value = response.totalElements
    loading.value = false
  } catch (error) {
    ElMessage.error($t('errorMessage.fetchUserListFailed'))
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  fetchUsers()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  fetchUsers()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchUsers()
}

// 组件挂载时获取用户列表
onMounted(() => {
  fetchUsers()
})

const handleAddUser = () => {
  dialogTitle.value = $t('dialog.addUser')
  form.value = {
    username: '',
    nickname: '',
    email: '',
    phone: '',
    role: 'user',
    status: 'ACTIVE'
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = $t('dialog.editUser')
  form.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm($t('message.confirmDelete'), $t('message.warning'), {
    confirmButtonText: $t('button.confirm'),
    cancelButtonText: $t('button.cancel'),
    type: 'warning'
  })
    .then(async () => {
      try {
        await deleteUser(row.id)
        ElMessage.success($t('message.deleteSuccess'))
        fetchUsers()
      } catch (error) {
        ElMessage.error($t('errorMessage.deleteFailed'))
      }
    })
    .catch(() => {
      ElMessage.info($t('message.deleteCancel'))
    })
}

const handleBatchDelete = () => {
  if (selectedUsers.value.length === 0) return
  ElMessageBox.confirm($t('message.confirmBatchDelete'), $t('message.warning'), {
    confirmButtonText: $t('button.confirm'),
    cancelButtonText: $t('button.cancel'),
    type: 'warning'
  })
    .then(async () => {
      try {
        await Promise.all(selectedUsers.value.map(user => deleteUser(user.id)))
        ElMessage.success($t('message.batchDeleteSuccess'))
        fetchUsers()
      } catch (error) {
        ElMessage.error($t('errorMessage.batchDeleteFailed'))
      }
    })
    .catch(() => {
      ElMessage.info($t('message.deleteCancel'))
    })
}

const handleSelectionChange = (val) => {
  selectedUsers.value = val
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (form.value.id) {
          await updateUser(form.value.id, form.value)
          ElMessage.success($t('message.updateSuccess'))
        } else {
          await createUser(form.value)
          ElMessage.success($t('message.createSuccess'))
        }
        dialogVisible.value = false
        fetchUsers()
      } catch (error) {
        ElMessage.error(form.value.id ? $t('errorMessage.updateFailed') : $t('errorMessage.createFailed'))
      }
    }
  })
}
</script>

<style scoped>
.user-list {
  padding: 10px 20px;
  height: calc(100vh - 140px);
  display: flex;
  flex-direction: column;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

.page-title {
  font-size: 20px;
  color: #2196F3;
  font-family: "Microsoft YaHei", "PingFang SC", "Hiragino Sans GB", "Helvetica Neue", Arial, sans-serif;
  margin-top: 10px;
}

.operation-bar {
  margin-top: 0;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.search-input {
  width: 400px;
  margin-right: auto;
}

.el-table {
  flex: 1;
  overflow: auto;
  margin-bottom: 5px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 9px;
  flex-shrink: 0;
}

.operation-buttons {
  display: flex;
  align-items: center;
  gap: 8px;
}

.operation-buttons .el-button {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.operation-buttons .el-icon {
  margin-right: 4px;
}
</style>@/utils/layoutFormat