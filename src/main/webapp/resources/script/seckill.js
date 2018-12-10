//存放主要交互逻辑js代码
// javascript 模块化(分包)
var seckill = {
    //封装秒杀相关ajax的url
    URL: {
        now: '/seckill/time/now',
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }
    },
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },
    handleSeckill: function (seckillId, node) {
        //获取秒杀地址，控制显示逻辑，执行秒杀
        var startHtml = '<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button> ';
        node.hide().html(startHtml);
        $.post(seckill.URL.exposer(seckillId), {}, function (res) {
            //在回调函数中执行交互流程
            if (res && res['success']) {
                var exposer = res['data'];
                if (exposer['exposed']) {
                    //开启秒杀
                    //获取秒杀地址
                    var md5 = exposer['md5']
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log("killUrl:" + killUrl);
                    $('#killBtn').one('click', function () {
                        //绑定执行秒杀请求的操作
                        //1:先禁用按钮
                        $(this).addClass('disabled');
                        //2：发送秒杀请求执行秒杀
                        $.post(killUrl, {}, function (res) {
                            if (res && res['success']) {
                                var killResult = res['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //3：显示秒杀结果
                                var resHtml = '<span class="label label-success">' + stateInfo + '</span>'
                                node.html(resHtml);
                            } else {
                                console.log(res);
                            }
                        });
                    });
                    node.show();
                } else {
                    //未开启秒杀
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //重新计算计时逻辑
                    seckill.countdown(seckillId, now, start, end);
                }
            } else {
                console.log('res:' + res);
            }
        })
    },
    countdown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        //时间判断
        if (nowTime > endTime) {
            //秒杀结束
            seckillBox.html('秒杀结束！');
        } else if (nowTime < startTime) {
            //秒杀未开始，计时事件绑定
            var killTime = new Date(startTime + 1000)
            seckillBox.countdown(killTime, function (event) {
                //时间格式
                var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
                //时间完成后回调事件
            }).on('finish.countdown', function () {
                //获取秒杀地址，控制显示逻辑，执行秒杀
                seckill.handleSeckill(seckillId, seckillBox);
            });
        } else {
            //秒杀开始
            seckill.handleSeckill(seckillId, seckillBox);
        }
    },
    //详情页秒杀逻辑
    detail: {
        //详情页初始化
        init: function (params) {
            //手机验证和登录,计时交互
            //规划我们的交互流程
            //在cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            //验证手机号
            if (!seckill.validatePhone(killPhone)) {
                //绑定phone
                //控制输出
                var killkillPhoneModal = $('#killPhoneModal');
                killkillPhoneModal.modal({
                    //显示弹出层
                    show: true,
                    //禁止位置关闭
                    backdrop: 'static',
                    //关闭键盘事件
                    keyboard: false
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if (seckill.validatePhone(inputPhone)) {
                        //电话写入cookie
                        $.cookie('killPhone', inputPhone, {
                            expires: 7,
                            path: '/seckill'
                        })
                        //刷新页面
                        window.location.reload();
                    } else {
                        var msgHtml = '<label class="label label-danger">手机号错误！</label>'
                        $('#killPhoneMessage').hide().html(msgHtml).show(300);
                    }
                })
            }
            //已经登录,计时交互
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            $.get(seckill.URL.now, {}, function (res) {
                if (res && res['success']) {
                    var nowTime = res['data'];
                    //时间判断
                    seckill.countdown(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log('res:' + res);
                }
            })
        }
    }
}