var PCA = PCA || {};
PCA = {
	createLi : function(name, value, index) {
		return $('<li data-value="' + value + '" data-index="' + index + '"><a>' + name + '</a></li>');
	},
	choseLi : function(li) {
		var parent = li.parent();
		var last_li = parent.find('li.select');
		var last_index = parseInt(last_li.attr('data-index')) - 1;
		if (last_li) {
			last_li.removeClass("select");
			if (last_index > -1) {
				last_li.insertAfter(parent.find('li[data-index="' + last_index + '"]'));
			}
		}
		li.addClass("select").prependTo(parent);
		li.closest(".default_select").find(".select_text span").text(li.attr('data-value'));
	},
	showcity : function(provinceid, cityid, province, city) {
		var cityObject = $('<ul name="' + cityid + '" id="' + cityid + '"></ul>');
		$('#city_select').html(cityObject);
		if (!province || province == '') {
			$('#city_select_area').hide();
		} else {
			$('#city_select_area').show();
			$()
			switch (province) {
			case '安徽':
				cityLis = new Array('合肥', '安庆', '蚌埠', '亳州', '巢湖', '滁州', '阜阳', '贵池', '淮北', '淮化', '淮南', '黄山', '九华山', '六安', '马鞍山', '宿州', '铜陵', '屯溪', '芜湖', '宣城');
				break;
			case '北京':
				cityLis = new Array('东城', '西城', '崇文', '宣武', '朝阳', '丰台', '石景山', '海淀', '门头沟', '房山', '通州', '顺义', '昌平', '大兴', '平谷', '怀柔', '密云', '延庆');
				break;
			case '重庆':
				cityLis = new Array('万州', '涪陵', '渝中', '大渡口', '江北', '沙坪坝', '九龙坡', '南岸', '北碚', '万盛', '双挢', '渝北', '巴南', '黔江', '长寿', '綦江', '潼南', '铜梁', '大足', '荣昌', '壁山', '梁平', '城口', '丰都', '垫江', '武隆', '忠县', '开县', '云阳', '奉节', '巫山', '巫溪', '石柱', '秀山', '酉阳', '彭水', '江津', '合川', '永川', '南川');
				break;
			case '福建':
				cityLis = new Array('福州', '福安', '龙岩', '南平', '宁德', '莆田', '泉州', '三明', '邵武', '石狮', '晋江', '永安', '武夷山', '厦门', '漳州');
				break;
			case '甘肃':
				cityLis = new Array('兰州', '白银', '定西', '敦煌', '甘南', '金昌', '酒泉', '临夏', '平凉', '天水', '武都', '武威', '西峰', '嘉峪关', '张掖');
				break;
			case '广东':
				cityLis = new Array('广州', '潮阳', '潮州', '澄海', '东莞', '佛山', '河源', '惠州', '江门', '揭阳', '开平', '茂名', '梅州', '清远', '汕头', '汕尾', '韶关', '深圳', '顺德', '阳江', '英德', '云浮', '增城', '湛江', '肇庆', '中山', '珠海');
				break;
			case '广西':
				cityLis = new Array('南宁', '百色', '北海', '桂林', '防城港', '河池', '贺州', '柳州', '来宾', '钦州', '梧州', '贵港', '玉林');
				break;
			case '贵州':
				cityLis = new Array('贵阳', '安顺', '毕节', '都匀', '凯里', '六盘水', '铜仁', '兴义', '玉屏', '遵义');
				break;
			case '海南':
				cityLis = new Array('海口', '三亚', '五指山', '琼海', '儋州', '文昌', '万宁', '东方', '定安', '屯昌', '澄迈', '临高', '万宁', '白沙黎族', '昌江黎族', '乐东黎族', '陵水黎族', '保亭黎族', '琼中黎族', '西沙群岛', '南沙群岛', '中沙群岛');
				break;
			case '河北':
				cityLis = new Array('石家庄', '保定', '北戴河', '沧州', '承德', '丰润', '邯郸', '衡水', '廊坊', '南戴河', '秦皇岛', '唐山', '新城', '邢台', '张家口');
				break;
			case '黑龙江':
				cityLis = new Array('哈尔滨', '北安', '大庆', '大兴安岭', '鹤岗', '黑河', '佳木斯', '鸡西', '牡丹江', '齐齐哈尔', '七台河', '双鸭山', '绥化', '伊春');
				break;
			case '河南':
				cityLis = new Array('郑州', '安阳', '鹤壁', '潢川', '焦作', '济源', '开封', '漯河', '洛阳', '南阳', '平顶山', '濮阳', '三门峡', '商丘', '新乡', '信阳', '许昌', '周口', '驻马店');
				break;
			case '湖北':
				cityLis = new Array('武汉', '恩施', '鄂州', '黄冈', '黄石', '荆门', '荆州', '潜江', '十堰', '随州', '武穴', '仙桃', '咸宁', '襄阳', '孝感', '宜昌');
				break;
			case '湖南':
				cityLis = new Array('长沙', '常德', '郴州', '衡阳', '怀化', '吉首', '娄底', '邵阳', '湘潭', '益阳', '岳阳', '永州', '张家界', '株洲');
				break;
			case '江苏':
				cityLis = new Array('南京', '常熟', '常州', '海门', '淮安', '江都', '江阴', '昆山', '连云港', '南通', '启东', '沭阳', '宿迁', '苏州', '太仓', '泰州', '同里', '无锡', '徐州', '盐城', '扬州', '宜兴', '仪征', '张家港', '镇江', '周庄');
				break;
			case '江西':
				cityLis = new Array('南昌', '抚州', '赣州', '吉安', '景德镇', '井冈山', '九江', '庐山', '萍乡', '上饶', '新余', '宜春', '鹰潭');
				break;
			case '吉林':
				cityLis = new Array('长春', '白城', '白山', '珲春', '辽源', '梅河', '吉林', '四平', '松原', '通化', '延吉');
				break;
			case '辽宁':
				cityLis = new Array('沈阳', '鞍山', '本溪', '朝阳', '大连', '丹东', '抚顺', '阜新', '葫芦岛', '锦州', '辽阳', '盘锦', '铁岭', '营口');
				break;
			case '内蒙古':
				cityLis = new Array('呼和浩特', '阿拉善盟', '包头', '赤峰', '东胜', '海拉尔', '集宁', '临河', '通辽', '乌海', '乌兰浩特', '锡林浩特');
				break;
			case '宁夏':
				cityLis = new Array('银川', '固原', '中卫', '石嘴山', '吴忠');
				break;
			case '青海':
				cityLis = new Array('西宁', '德令哈', '格尔木', '共和', '海东', '海晏', '玛沁', '同仁', '玉树');
				break;
			case '山东':
				cityLis = new Array('济南', '滨州', '兖州', '德州', '东营', '菏泽', '济宁', '莱芜', '聊城', '临沂', '蓬莱', '青岛', '曲阜', '日照', '泰安', '潍坊', '威海', '烟台', '枣庄', '淄博');
				break;
			case '上海':
				cityLis = new Array('崇明', '黄浦', '卢湾', '徐汇', '长宁', '静安', '普陀', '闸北', '虹口', '杨浦', '闵行', '宝山', '嘉定', '浦东', '金山', '松江', '青浦', '南汇', '奉贤', '朱家角');
				break;
			case '山西':
				cityLis = new Array('太原', '长治', '大同', '候马', '晋城', '离石', '临汾', '宁武', '朔州', '忻州', '阳泉', '榆次', '运城');
				break;
			case '陕西':
				cityLis = new Array('西安', '安康', '宝鸡', '汉中', '渭南', '商州', '绥德', '铜川', '咸阳', '延安', '榆林');
				break;
			case '四川':
				cityLis = new Array('成都', '巴中', '达州', '德阳', '都江堰', '峨眉山', '涪陵', '广安', '广元', '九寨沟', '康定', '乐山', '泸州', '马尔康', '绵阳', '眉山', '南充', '内江', '攀枝花', '遂宁', '汶川', '西昌', '雅安', '宜宾', '自贡', '资阳');
				break;
			case '天津':
				cityLis = new Array('天津', '和平', '东丽', '河东', '西青', '河西', '津南', '南开', '北辰', '河北', '武清', '红挢', '塘沽', '汉沽', '大港', '宁河', '静海', '宝坻', '蓟县');
				break;
			case '新疆':
				cityLis = new Array('乌鲁木齐', '阿克苏', '阿勒泰', '阿图什', '博乐', '昌吉', '东山', '哈密', '和田', '喀什', '克拉玛依', '库车', '库尔勒', '奎屯', '石河子', '塔城', '吐鲁番', '伊宁');
				break;
			case '西藏':
				cityLis = new Array('拉萨', '阿里', '昌都', '林芝', '那曲', '日喀则', '山南');
				break;
			case '云南':
				cityLis = new Array('昆明', '大理', '保山', '楚雄', '大理', '东川', '个旧', '景洪', '开远', '临沧', '丽江', '六库', '潞西', '曲靖', '思茅', '文山', '西双版纳', '玉溪', '中甸', '昭通');
				break;
			case '浙江':
				cityLis = new Array('杭州', '安吉', '慈溪', '定海', '奉化', '海盐', '黄岩', '湖州', '嘉兴', '金华', '临安', '临海', '丽水', '宁波', '瓯海', '平湖', '千岛湖', '衢州', '江山', '瑞安', '绍兴', '嵊州', '台州', '温岭', '温州', '余姚', '舟山');
				break;
			case '香港':
				cityLis = new Array('香港', '九龙', '新界');
				break;
			case '澳门':
				cityLis = new Array('澳门', '澳门');
				break;
			case '台湾':
				cityLis = new Array('台北', '基隆', '台南', '台中', '高雄', '屏东', '南投', '云林', '新竹', '彰化', '苗栗', '嘉义', '花莲', '桃园', '宜兰', '台东', '金门', '马祖', '澎湖', '其它');
				break;
			case '海外':
				cityLis = new Array('美国', '英国', '法国', '瑞士', '澳洲', '新西兰', '加拿大', '奥地利', '韩国', '日本', '德国', '意大利', '西班牙', '俄罗斯', '泰国', '印度', '荷兰', '新加坡', '欧洲', '北美', '南美', '亚洲', '非洲', '大洋洲');
				break;
			}
			var chosed = false;
			for ( var i = 0; i < cityLis.length; i++) {
				var _city = cityLis[i];
				var li = PCA.createLi(_city, _city, i);
				cityObject.append(li);
				li.click(function() {
					PCA.choseLi($(this));
				});
				if (city && _city == city) {
					li.click();
					chosed = true;
				}
			}
			if (!chosed) {
				PCA.choseLi(cityObject.find('li:first'));
			}
		}
	},
	showprovince : function(provinceid, cityid, province, city) {
		var provinceLis = new Array('北京', '上海', '重庆', '安徽', '福建', '甘肃', '广东', '广西', '贵州', '海南', '河北', '黑龙江', '河南', '香港', '湖北', '湖南', '江苏', '江西', '吉林', '辽宁', '澳门', '内蒙古', '宁夏', '青海', '山东', '山西', '陕西', '四川', '台湾', '天津', '新疆', '西藏', '云南', '浙江', '海外');
		var provinceObj = $('<ul name="' + provinceid + '" id="' + provinceid + '"></ul>');
		$('#province_select').html(provinceObj);
		for ( var i = 0; i < provinceLis.length; i++) {
			var _province = provinceLis[i];
			var li = PCA.createLi(_province, _province, i);
			provinceObj.append(li);
			li.click(function() {
				var self = $(this);
				PCA.choseLi(self);
				PCA.showcity(provinceid, cityid, self.attr('data-value'));
			});
			if (province && _province == province) {
				li.click();
			}
		}
	}
}