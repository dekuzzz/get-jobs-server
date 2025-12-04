#!/usr/bin/env python3
"""
数据库迁移脚本 - 添加role字段
使用方法: python3 migrate_database.py
"""

import psycopg2
import sys
import os

# 从环境变量或配置文件读取数据库连接信息
DB_HOST = os.getenv('DB_HOST', 'localhost')
DB_PORT = os.getenv('DB_PORT', '5432')
DB_NAME = os.getenv('DB_NAME', 'postgres')
DB_USER = os.getenv('DB_USER', 'postgres')
DB_PASSWORD = os.getenv('DB_PASSWORD', 'WCwsad123456')

def execute_migration():
    """执行数据库迁移"""
    try:
        print("正在连接数据库...")
        conn = psycopg2.connect(
            host=DB_HOST,
            port=DB_PORT,
            database=DB_NAME,
            user=DB_USER,
            password=DB_PASSWORD
        )
        conn.autocommit = True
        cursor = conn.cursor()
        
        print("✓ 数据库连接成功")
        
        # 1. 添加role字段
        print("\n步骤1: 添加role字段...")
        cursor.execute("""
            ALTER TABLE user_accounts 
            ADD COLUMN IF NOT EXISTS role VARCHAR(20) CHECK (role IN ('job_seeker', 'recruiters')) DEFAULT 'job_seeker';
        """)
        print("✓ role字段添加成功")
        
        # 2. 为现有数据设置默认role（根据talents表）
        print("\n步骤2: 为talents表中的用户设置role为job_seeker...")
        cursor.execute("""
            UPDATE user_accounts ua
            SET role = 'job_seeker'
            WHERE ua.role IS NULL 
              AND EXISTS (SELECT 1 FROM talents t WHERE t.user_id = ua.user_id);
        """)
        affected = cursor.rowcount
        print(f"✓ 更新了 {affected} 条记录（job_seeker）")
        
        # 3. 为现有数据设置默认role（根据recruiters表）
        print("\n步骤3: 为recruiters表中的用户设置role为recruiters...")
        cursor.execute("""
            UPDATE user_accounts ua
            SET role = 'recruiters'
            WHERE ua.role IS NULL 
              AND EXISTS (SELECT 1 FROM recruiters r WHERE r.user_id = ua.user_id);
        """)
        affected = cursor.rowcount
        print(f"✓ 更新了 {affected} 条记录（recruiters）")
        
        # 4. 为其他用户设置默认role
        print("\n步骤4: 为其他用户设置默认role为job_seeker...")
        cursor.execute("""
            UPDATE user_accounts ua
            SET role = 'job_seeker'
            WHERE ua.role IS NULL;
        """)
        affected = cursor.rowcount
        print(f"✓ 更新了 {affected} 条记录（默认job_seeker）")
        
        # 5. 验证迁移结果
        print("\n步骤5: 验证迁移结果...")
        cursor.execute("""
            SELECT 
                COUNT(*) as total,
                COUNT(CASE WHEN role = 'job_seeker' THEN 1 END) as job_seekers,
                COUNT(CASE WHEN role = 'recruiters' THEN 1 END) as recruiters,
                COUNT(CASE WHEN role IS NULL THEN 1 END) as null_roles
            FROM user_accounts;
        """)
        result = cursor.fetchone()
        print(f"✓ 总用户数: {result[0]}")
        print(f"✓ 求职者(job_seeker): {result[1]}")
        print(f"✓ 招聘者(recruiters): {result[2]}")
        print(f"✓ 未设置role: {result[3]}")
        
        # 检查表结构
        cursor.execute("""
            SELECT column_name, data_type, column_default
            FROM information_schema.columns
            WHERE table_name = 'user_accounts' AND column_name = 'role';
        """)
        column_info = cursor.fetchone()
        if column_info:
            print(f"\n✓ role字段信息:")
            print(f"  - 字段名: {column_info[0]}")
            print(f"  - 数据类型: {column_info[1]}")
            print(f"  - 默认值: {column_info[2]}")
        
        cursor.close()
        conn.close()
        
        print("\n" + "="*50)
        print("✓ 数据库迁移完成！")
        print("="*50)
        return True
        
    except psycopg2.Error as e:
        print(f"\n✗ 数据库错误: {e}")
        return False
    except Exception as e:
        print(f"\n✗ 发生错误: {e}")
        import traceback
        traceback.print_exc()
        return False

if __name__ == "__main__":
    print("="*50)
    print("数据库迁移脚本 - 添加role字段")
    print("="*50)
    print(f"\n数据库配置:")
    print(f"  主机: {DB_HOST}")
    print(f"  端口: {DB_PORT}")
    print(f"  数据库: {DB_NAME}")
    print(f"  用户: {DB_USER}")
    print("\n开始执行迁移...")
    
    success = execute_migration()
    
    if success:
        sys.exit(0)
    else:
        sys.exit(1)

