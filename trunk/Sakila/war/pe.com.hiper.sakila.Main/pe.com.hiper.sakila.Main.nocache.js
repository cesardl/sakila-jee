function pe_com_hiper_sakila_Main(){var O='',vb='" for "gwt:onLoadErrorFn"',tb='" for "gwt:onPropertyErrorFn"',hb='"><\/script>',Y='#',$b='&',uc='.cache.html',$='/',tc=':',nb='::',Cc='<script defer="defer">pe_com_hiper_sakila_Main.onInjectionDone(\'pe.com.hiper.sakila.Main\')<\/script>',gb='<script id="',qb='=',Z='?',fc='ActiveXObject',sb='Bad handler "',gc='ChromeTab.ChromeFrame',Bc='DOMContentLoaded',sc="GWT module 'pe.com.hiper.sakila.Main' may need to be (re)compiled",ib='SCRIPT',bc='Unexpected exception in locale detection, using default: ',ac='_',_b='__gwt_Locale',fb='__gwt_marker_pe.com.hiper.sakila.Main',Vb='adobeair',Wb='air',jb='base',bb='baseUrl',S='begin',R='bootstrap',Bb='chrome',ec='chromeframe',ab='clear.cache.gif',pb='content',Yb='default',X='end',Rb='gecko',Tb='gecko1_8',Ub='gecko1_9',T='gwt.codesvr=',U='gwt.hosted=',V='gwt.hybrid',vc='gwt/chrome/chrome.css',ub='gwt:onLoadErrorFn',rb='gwt:onPropertyErrorFn',ob='gwt:property',Ab='gxt.user.agent',Ac='head',qc='hosted.html?pe_com_hiper_sakila_Main',zc='href',Fb='ie6',Hb='ie7',Jb='ie8',Kb='ie9',wb='iframe',_='img',xb="javascript:''",wc='link',mc='linux',pc='loadExternalRefs',Xb='locale',Zb='locale=',lc='mac',kc='mac os x',jc='macintosh',kb='meta',zb='moduleRequested',W='moduleStartup',Db='msie',Eb='msie 6',Gb='msie 7',Ib='msie 8',lb='name',Cb='opera',P='pe.com.hiper.sakila.Main',db='pe.com.hiper.sakila.Main.nocache.js',mb='pe.com.hiper.sakila.Main::',yb='position:absolute;width:0;height:0;border:none',xc='rel',Sb='rv:1.8',Lb='safari',Nb='safari3',Pb='safari4',Qb='safari5',cb='script',rc='selectingPermutation',Q='startup',yc='stylesheet',eb='undefined',hc='unknown',cc='user.agent',ic='user.agent.os',Mb='version/3',Ob='version/4',dc='webkit',oc='win32',nc='windows';var l=window,m=document,n=l.__gwtStatsEvent?function(a){return l.__gwtStatsEvent(a)}:null,o=l.__gwtStatsSessionId?l.__gwtStatsSessionId:null,p,q,r,s=O,t={},u=[],v=[],w=[],x=0,y,z;n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:R,millis:(new Date).getTime(),type:S});if(!l.__gwt_stylesLoaded){l.__gwt_stylesLoaded={}}if(!l.__gwt_scriptsLoaded){l.__gwt_scriptsLoaded={}}function A(){var b=false;try{var c=l.location.search;return (c.indexOf(T)!=-1||(c.indexOf(U)!=-1||l.external&&l.external.gwtOnLoad))&&c.indexOf(V)==-1}catch(a){}A=function(){return b};return b}
function B(){if(p&&q){var b=m.getElementById(P);var c=b.contentWindow;if(A()){c.__gwt_getProperty=function(a){return G(a)}}pe_com_hiper_sakila_Main=null;c.gwtOnLoad(y,P,s,x);n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:W,millis:(new Date).getTime(),type:X})}}
function C(){function e(a){var b=a.lastIndexOf(Y);if(b==-1){b=a.length}var c=a.indexOf(Z);if(c==-1){c=a.length}var d=a.lastIndexOf($,Math.min(c,b));return d>=0?a.substring(0,d+1):O}
function f(a){if(a.match(/^\w+:\/\//)){}else{var b=m.createElement(_);b.src=a+ab;a=e(b.src)}return a}
function g(){var a=F(bb);if(a!=null){return a}return O}
function h(){var a=m.getElementsByTagName(cb);for(var b=0;b<a.length;++b){if(a[b].src.indexOf(db)!=-1){return e(a[b].src)}}return O}
function i(){var a;if(typeof isBodyLoaded==eb||!isBodyLoaded()){var b=fb;var c;m.write(gb+b+hb);c=m.getElementById(b);a=c&&c.previousSibling;while(a&&a.tagName!=ib){a=a.previousSibling}if(c){c.parentNode.removeChild(c)}if(a&&a.src){return e(a.src)}}return O}
function j(){var a=m.getElementsByTagName(jb);if(a.length>0){return a[a.length-1].href}return O}
var k=g();if(k==O){k=h()}if(k==O){k=i()}if(k==O){k=j()}if(k==O){k=e(m.location.href)}k=f(k);s=k;return k}
function D(){var b=document.getElementsByTagName(kb);for(var c=0,d=b.length;c<d;++c){var e=b[c],f=e.getAttribute(lb),g;if(f){f=f.replace(mb,O);if(f.indexOf(nb)>=0){continue}if(f==ob){g=e.getAttribute(pb);if(g){var h,i=g.indexOf(qb);if(i>=0){f=g.substring(0,i);h=g.substring(i+1)}else{f=g;h=O}t[f]=h}}else if(f==rb){g=e.getAttribute(pb);if(g){try{z=eval(g)}catch(a){alert(sb+g+tb)}}}else if(f==ub){g=e.getAttribute(pb);if(g){try{y=eval(g)}catch(a){alert(sb+g+vb)}}}}}}
function E(a,b){return b in u[a]}
function F(a){var b=t[a];return b==null?null:b}
function G(a){var b=v[a](),c=u[a];if(b in c){return b}var d=[];for(var e in c){d[c[e]]=e}if(z){z(a,d,b)}throw null}
var H;function I(){if(!H){H=true;var a=m.createElement(wb);a.src=xb;a.id=P;a.style.cssText=yb;a.tabIndex=-1;m.body.appendChild(a);n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:W,millis:(new Date).getTime(),type:zb});a.contentWindow.location.replace(s+K)}}
v[Ab]=function(){var a=navigator.userAgent.toLowerCase();if(a.indexOf(Bb)!=-1)return Bb;if(a.indexOf(Cb)!=-1)return Cb;if(a.indexOf(Db)!=-1){if(a.indexOf(Eb)!=-1)return Fb;if(a.indexOf(Gb)!=-1)return Hb;if(a.indexOf(Ib)!=-1)return Jb;return Kb}if(a.indexOf(Lb)!=-1){if(a.indexOf(Mb)!=-1)return Nb;if(a.indexOf(Ob)!=-1)return Pb;return Qb}if(a.indexOf(Rb)!=-1){if(a.indexOf(Sb)!=-1)return Tb;return Ub}if(a.indexOf(Vb)!=-1)return Wb;return null};u[Ab]={air:0,chrome:1,gecko1_8:2,gecko1_9:3,ie6:4,ie7:5,ie8:6,ie9:7,opera:8,safari3:9,safari4:10,safari5:11};v[Xb]=function(){var b=null;var c=Yb;try{if(!b){var d=location.search;var e=d.indexOf(Zb);if(e>=0){var f=d.substring(e+7);var g=d.indexOf($b,e);if(g<0){g=d.length}b=d.substring(e+7,g)}}if(!b){b=F(Xb)}if(!b){b=l[_b]}if(b){c=b}while(b&&!E(Xb,b)){var h=b.lastIndexOf(ac);if(h<0){b=null;break}b=b.substring(0,h)}}catch(a){alert(bc+a)}l[_b]=c;return b||Yb};u[Xb]={'default':0,es:1};v[cc]=function(){var c=navigator.userAgent.toLowerCase();var d=function(a){return parseInt(a[1])*1000+parseInt(a[2])};if(function(){return c.indexOf(Cb)!=-1}())return Cb;if(function(){return c.indexOf(dc)!=-1||function(){if(c.indexOf(ec)!=-1){return true}if(typeof window[fc]!=eb){try{var b=new ActiveXObject(gc);if(b){b.registerBhoIfNeeded();return true}}catch(a){}}return false}()}())return Lb;if(function(){return c.indexOf(Db)!=-1&&m.documentMode>=9}())return Kb;if(function(){return c.indexOf(Db)!=-1&&m.documentMode>=8}())return Jb;if(function(){var a=/msie ([0-9]+)\.([0-9]+)/.exec(c);if(a&&a.length==3)return d(a)>=6000}())return Fb;if(function(){return c.indexOf(Rb)!=-1}())return Tb;return hc};u[cc]={gecko1_8:0,ie6:1,ie8:2,ie9:3,opera:4,safari:5};v[ic]=function(){var a=l.navigator.userAgent.toLowerCase();if(a.indexOf(jc)!=-1||a.indexOf(kc)!=-1){return lc}if(a.indexOf(mc)!=-1){return mc}if(a.indexOf(nc)!=-1||a.indexOf(oc)!=-1){return nc}return hc};u[ic]={linux:0,mac:1,windows:2};pe_com_hiper_sakila_Main.onScriptLoad=function(){if(H){q=true;B()}};pe_com_hiper_sakila_Main.onInjectionDone=function(){p=true;n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:pc,millis:(new Date).getTime(),type:X});B()};D();C();var J;var K;if(A()){if(l.external&&(l.external.initModule&&l.external.initModule(P))){l.location.reload();return}K=qc;J=O}n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:R,millis:(new Date).getTime(),type:rc});if(!A()){try{alert(sc);return;var L=J.indexOf(tc);if(L!=-1){x=Number(J.substring(L+1));J=J.substring(0,L)}K=J+uc}catch(a){return}}var M;function N(){if(!r){r=true;if(!__gwt_stylesLoaded[vc]){var a=m.createElement(wc);__gwt_stylesLoaded[vc]=a;a.setAttribute(xc,yc);a.setAttribute(zc,s+vc);m.getElementsByTagName(Ac)[0].appendChild(a)}B();if(m.removeEventListener){m.removeEventListener(Bc,N,false)}if(M){clearInterval(M)}}}
if(m.addEventListener){m.addEventListener(Bc,function(){I();N()},false)}var M=setInterval(function(){if(/loaded|complete/.test(m.readyState)){I();N()}},50);n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:R,millis:(new Date).getTime(),type:X});n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:pc,millis:(new Date).getTime(),type:S});m.write(Cc)}
pe_com_hiper_sakila_Main();