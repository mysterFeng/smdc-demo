#!/bin/bash

# 智能点餐小程序前后端联调测试脚本
# 使用方法: ./联调测试脚本.sh

echo "=========================================="
echo "智能点餐小程序前后端联调测试"
echo "测试时间: $(date)"
echo "=========================================="

# 基础URL
BASE_URL="http://localhost:8080/api"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 测试函数
test_api() {
    local name="$1"
    local method="$2"
    local url="$3"
    local data="$4"
    
    echo -e "\n${YELLOW}测试: $name${NC}"
    echo "请求: $method $url"
    
    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "\nHTTP状态码: %{http_code}" "$BASE_URL$url" -H "Content-Type: application/json")
    else
        response=$(curl -s -w "\nHTTP状态码: %{http_code}" -X "$method" "$BASE_URL$url" -H "Content-Type: application/json" -d "$data")
    fi
    
    # 提取HTTP状态码
    http_code=$(echo "$response" | tail -n1 | awk '{print $2}')
    response_body=$(echo "$response" | head -n -1)
    
    if [ "$http_code" = "200" ]; then
        echo -e "${GREEN}✅ 成功 (HTTP $http_code)${NC}"
        echo "响应: $response_body" | head -c 200
        echo "..."
    else
        echo -e "${RED}❌ 失败 (HTTP $http_code)${NC}"
        echo "响应: $response_body"
    fi
}

# 检查服务是否运行
echo -e "\n${YELLOW}检查后端服务状态...${NC}"
if curl -s "$BASE_URL/v1/users/test" > /dev/null; then
    echo -e "${GREEN}✅ 后端服务运行正常${NC}"
else
    echo -e "${RED}❌ 后端服务未运行或无法访问${NC}"
    exit 1
fi

# 1. 用户模块测试
echo -e "\n${YELLOW}=== 用户模块测试 ===${NC}"
test_api "基础测试接口" "GET" "/v1/users/test"
test_api "手机号登录" "POST" "/v1/users/phone-login" '{"phone":"13800000001","password":"123456"}'

# 2. 分类模块测试
echo -e "\n${YELLOW}=== 分类模块测试 ===${NC}"
test_api "获取活跃分类" "GET" "/v1/categories/active"

# 3. 菜品模块测试
echo -e "\n${YELLOW}=== 菜品模块测试 ===${NC}"
test_api "菜品列表分页" "GET" "/v1/dishes?page=0&size=5"
test_api "推荐菜品" "GET" "/v1/dishes/recommend?limit=4"
test_api "按分类获取菜品" "GET" "/v1/dishes?categoryId=1&page=0&size=5"

# 4. 购物车模块测试
echo -e "\n${YELLOW}=== 购物车模块测试 ===${NC}"
test_api "获取用户购物车" "GET" "/v1/cart/2/items"
test_api "获取购物车数量" "GET" "/v1/cart/2/count"

# 5. 订单模块测试
echo -e "\n${YELLOW}=== 订单模块测试 ===${NC}"
test_api "获取用户订单" "GET" "/v1/orders/user/2?page=0&size=5"

# 6. 添加购物车测试
echo -e "\n${YELLOW}=== 购物车操作测试 ===${NC}"
test_api "添加菜品到购物车" "POST" "/v1/cart/2/items" '{"dishId":1,"quantity":1,"remark":""}'

echo -e "\n${GREEN}=========================================="
echo "联调测试完成！"
echo "==========================================${NC}"

# 生成测试报告
echo -e "\n${YELLOW}生成测试报告...${NC}"
cat > 联调测试结果_$(date +%Y%m%d_%H%M%S).txt << EOF
智能点餐小程序前后端联调测试结果
测试时间: $(date)
测试状态: 完成

测试项目:
1. 用户模块 - 基础测试接口、手机号登录
2. 分类模块 - 获取活跃分类
3. 菜品模块 - 菜品列表、推荐菜品、分类菜品
4. 购物车模块 - 获取购物车、购物车数量
5. 订单模块 - 获取用户订单
6. 购物车操作 - 添加菜品到购物车

注意事项:
- 请检查响应数据格式是否正确
- 确认业务逻辑是否符合预期
- 验证错误处理是否完善

EOF

echo -e "${GREEN}测试报告已生成${NC}" 