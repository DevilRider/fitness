## 功能
### 初次进入
	填写个人信息page1，选择角色page2 
### 之后进入
	根据不同角色进入不同功能。
### 会员
	选择责任教练，变更责任教练page3 （初次进入）
	查看教练课表（默认责任教练课表）page4，可切换教练（教练列表）
	进行约课，取消，交换（page5） fn4.1 fn4.2 fn5.1 
### 教练
	训练编排 page6（每节课的开始结束时间）
	    训练编排初始化 fn6.1
		训练添加 fn6.2
		训练取消 fn6.3
		训练变更，变更需会员确认才生效 fn6.4（fn5.1）
### 管理员
	home：page7
		new member count 待确认会员数，点击进入后进行会员确认 page7 
		active member count 活跃会员数 ：从本周周日向前推一周戒指到当天内来上过课的会员数 

		本日day 会员约课数，本周week 会员累计约课数，本月month 会员累计约课数
		
		会员管理/教练管理（教练确认在此页面中加入跳转）
		
		各教练上课数 （日/周/月） table/chart
		
    会员管理 member 
		会员确认 confirm：新加入会员（开放约课授权）page8
		会员信息查看：个人信息及上课信息 page9

	教练管理 coach
		教练确认 confirm：新教练加盟（开放排班，课程变更权限）（page8）
		教练删除 delete 
		教练信息查看：个人信息及排班信息（page9）
### 所有用户 
	个人信息 profile，page 10
		基础信息 base page 11
		手机号 mobile page 12
		角色 role page 13
			change（如果是多角色则可以进行角色切换）
			apply（新申请角色）
### 短信通知
	确认通知- 会员1/教练1
	约课通知- 通知会员1/教练1
	交换通知- 通知会员2/教练1
## 对象
 * 用户/用户信息
 * 会员/教练/管理员/
 * 角色申请（会员申请/教练申请/管理员申请）
 * 角色/
 * 用户角色关联关系
 * 会员教练关联关系
 * 教练排班
## 实体
 * 用户 base_user
 * 用户信息 base_user_info
 * 角色 base_role (member/coach/admin)
 * 角色申请 biz_role_apply
 * 用户角色关联 base_user$role user_id/role_id/is_default
 * 会员教练关联关系 base_member$coach
 * 教练排班 biz_training_schedule: coach_id/start_time/end_time/member_id
 	* 教练/开始时间/结束时间/会员/备注 开始结束不得小于半小时
 * 教练排班变更申请 biz_training_schedule_exchange_apply:ts_id/apply_user/apply_role/member_id (一个申请两条记录)
 	* 涉及到两个会员的时候需要进行申请，不管发起人是会员还是教练都要发申请，涉事会员确认后生效
## actions
 * 会员/教练：users
 * 申请 apply
 * 排班 schedule
 * 确认 confirm
 * 约课 book
 * 变更 change
 * 交换 exchange






