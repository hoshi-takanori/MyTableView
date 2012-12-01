var MyTableView = require('com.example.MyTableView');

var win = Ti.UI.createWindow({
	backgroundColor:'white'
});

var descriptions = [
	'Lorem ipsum dolor sit amet,',
	'Lorem ipsum dolor sit amet, consectetur adipisicing elit,',
	'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt',
	'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
	'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,',
	'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco',
	'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip',
	'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.',
];

var data = [];
for (var i = 1; i <= 10; i++) {
	data.push('section ' + i);
	var cnt = (i <= 5) ? Math.floor(Math.random() * 10 + 1) : 0;
	for (var j = 1; j <= cnt; j++) {
		data.push({
			image:'images/' + 'ABCDEF'.substr(Math.floor(Math.random() * 6), 1) + '.png',
			title:'Hello, ' + i + '-' + j,
			description:descriptions[Math.floor(Math.random() * descriptions.length)],
		});
	}
}

var headerView = Ti.UI.createView({
	layout:'vertical',
	height:Ti.UI.SIZE,
});
headerView.add(Ti.UI.createView({ height:'4dip' }));
var label1 = Ti.UI.createLabel({
	text:'Hello, World!',
	font:{ fontSize:'16dip', fontWeight:'bold' },
	color:'#000',
	left:'10dip',
});
headerView.add(label1);
var label2 = Ti.UI.createLabel({
	text:'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.',
	font:{ fontSize:'12dip' },
	color:'#000',
	left:'10dip',
	right:'10dip',
});
headerView.add(label2);
headerView.add(Ti.UI.createView({ height:'4dip' }));

var tableView = MyTableView.createMyTableView({
	data:data,
	headerView:headerView,
	deleteButton:'削除',
});
tableView.addEventListener('click', function(e) {
	Ti.API.info('tableView click: index = ' + e.index);
});
tableView.addEventListener('delete', function(e) {
	Ti.API.info('tableView delete: index = ' + e.index);
});
win.add(tableView);

win.open();
