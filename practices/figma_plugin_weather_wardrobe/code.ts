const W=300,PID='03e180d5-d14e-41bd-81b5-8a4d229e728b',V='VERTICAL' as const,H='HORIZONTAL' as const
type Msg={type:'resize';height:number}|{type:'build-design-system';theme:'Dark'|'Light';removeCodeLayer:boolean;buildFoundations:boolean;buildComponents:boolean;buildScreens:boolean}
const post=(text:string,isError=false,done=false)=>figma.ui.postMessage({type:'status',text,isError,done})
const va=(v:Variable)=>figma.variables.createVariableAlias(v)
const bPnt=(v:Variable):SolidPaint=>figma.variables.setBoundVariableForPaint({type:'SOLID',color:{r:0,g:0,b:0}},'color',v)
const bFill=(n:GeometryMixin&MinimalFillsMixin,v?:Variable)=>{if(v)n.fills=[bPnt(v)]}
const bStrk=(n:GeometryMixin&MinimalStrokesMixin,v?:Variable,w=1,al:'INSIDE'|'OUTSIDE'|'CENTER'='INSIDE')=>{if(v){n.strokes=[bPnt(v)];n.strokeWeight=w;n.strokeAlign=al}}
const bRad=(n:CornerMixin&SceneNode,v?:Variable)=>{if(v&&'topLeftRadius' in n)for(const k of['topLeftRadius','topRightRadius','bottomLeftRadius','bottomRightRadius']as const)n.setBoundVariable(k,v)}
const bPad=(n:BaseFrameMixin,t:Variable,r=t,b=t,l=r)=>{n.setBoundVariable('paddingTop',t);n.setBoundVariable('paddingRight',r);n.setBoundVariable('paddingBottom',b);n.setBoundVariable('paddingLeft',l)}
const ICONS:Record<string,string>={
SunCloud:'M8 15a4 4 0 014-4h4.5a3.5 3.5 0 010 7H9a3 3 0 010-6M9 7.5A5.5 5.5 0 0118 10.6M12 3v1.5',
Hanger:'M12 7a2.5 2.5 0 10-2.5-2.5M12 7L3.2 13.6A1.5 1.5 0 004 16.3h16a1.5 1.5 0 00.8-2.7L12 7zM6 16.3V20h12v-3.7',
Scan:'M4 8V5h4M16 5h4v3M20 16v3h-4M8 19H4v-3M7 12h10',
User:'M12 12a4 4 0 100-8 4 4 0 000 8zM5 20a7 6 0 0114 0',
Jacket:'M9 4l3 2 3-2 4 2.5 2 10.5-3.5 1-1-7v9h-9v-9l-1 7L3 17l2-10.5L9 4zM12 6v14',
}
const SP_T:Array<[string,number]>=[0,2,4,8,12,16,24,32,40,48,64,80,96].map((v,i)=>[`spacing/${[0,25,50,75,100,200,300,400,500,600,700,800,900][i]}`,v])
const RD_T:Array<[string,number]>=[0,2,4,6,8,12,16,24,9999].map((v)=>[`radius/${v === 9999 ? 'full' : v}`,v])
const SH=['00','50','100','150','200','300','350','400','450','500','600','700','800','850','900','950','1000']
const CORE_P:Record<string,Array<[string,string,number]>>={
Grayscale:'00:FFFFFF,50:F6F6F7,100:ECECEE,150:E1E1E4,200:D4D4D8,250:C4C4CA,300:B0B0B8,400:9B9B9B,450:84848C,500:6E6E77,600:52525B,700:3F3F46,750:313131,800:272727,850:1F1F1F,870:181818,900:131209,950:0B0B0B,1000:000000'.split(',').map((s)=>[s.split(':')[0],`#${s.split(':')[1]}`,1]),
'Grayscale-Alpha':[16,24,40,64,80].map((a)=>[`700-${a}`,'#3F3F46',a/100]),
}
for(const[k,c]of Object.entries({Primary:[61,120,192],Secondary:[83,109,153],Accent:[110,80,176],Red:[176,65,65],Green:[56,140,95],Yellow:[168,121,40]})){
CORE_P[k]=SH.map((sh,i)=>{
const t=(i+0.4)/SH.length,h=c.map((v)=>Math.round(t<0.5?255 -(255 - v)*t*2:v*(1 -(t - 0.5)*1.85)).toString(16).padStart(2,'0')).join('')
return[sh,`#${h.toUpperCase()}`,1]
})
}
const PFX:Record<string,string>={G:'Grayscale/',P:'Primary/',R:'Red/',E:'Green/',Y:'Yellow/',A:'Accent/',X:'Grayscale-Alpha/'}
const WD=['Primary','Secondary','Tertiary','Disabled','Hover','Selected','Subtle','subtle','Inverted','Action','Error','Warning','Success','Info']
const expW=(s:string)=>s.replace(/\$([0-9a-d])/g,(_,c)=>WD[parseInt(c,16)])
const SEM_G:Record<string,string>={
Border:'$0:G750:G200,$0-$3:G800:G150,$0-light:G700:G250,$1:G750:G200,$2:G600:G300,$9-$0:P400:P500,$9-$3:P600:P200,$9-$1:G500:G400,$a:R400:R500,$a-disabled:R700:R200,$c:E400:E500,$b:Y300:Y500,$b - $3:Y700:Y200',
Surface:'$0:G870:G50,$0-$4:G800:G100,$0-$5:G700:G150,$0-$7:G850:G00,$1:G800:G00,$1-$4:G700:G100,$1-$5:G700:G150,$1-$6:G750:G100,$2:G600:G200,$2-$4:G450:G300,Accent:A500:A500,Accent-$7:A200:A100,$d:P400:P500,$d-$7:P150:P100,$a:R500:R500,$a-$7:R100:R50,$a - Darker:R700:R700,$8:G00:G870,$8-$4:G250:G800,$8-$3:G450:G600,$b:Y500:Y500,$b-$7:Y100:Y50,$b - $4:Y700:Y600,$c:E500:E500,$c-$6:E100:E50',
Action:'$0:P500:P600,$0-$4:P700:P700,$0-Click:P600:P800,$0-$3:P700:P200,$1:G600:G200,$1-$4:G750:G300,$1-Click:G700:G400,$a:R700:R600,$a-$4:R800:R700,$a-$3:R800:R200,$a-$6:R150:R100,$b:Y500:Y500,$b - $4:Y700:Y600,$b - $3:Y800:Y200,$b - $6:Y100:Y100',
Text:'$0:G50:G900,$0-$3:G750:G400,$0-$6:G500:G600,$1:G200:G700,$1-$3:G500:G400,$2:G400:G600,$8:G850:G00,$a:R500:R700,$a-$3:R800:R300,$a-$8:R800:R50,$a-$8-$3:R200:R200,$c:E600:E700,$c-$8:E700:E50,$c-$8-$3:E300:E200,$d:P350:P700,$d-$8:P700:P50,$d-$8-$3:P200:P200,$b:Y500:Y700,$b - $3:Y800:Y300,$b-$8:Y800:Y900,$b-$8-$3:Y300:Y300',
Icon:'Default:G50:G900,$1:G200:G700,$6:G500:G600,$8:G850:G00,$3:G750:G400,$a:R500:R600,$a-$3:R800:R300,$a-$8-$3:R200:R200,$d:P350:P600,$d-$3:P200:P300,$b:Y500:Y600,$b-$8:Y700:Y900,$b - $3:Y800:Y300,$b-$8-$3:Y300:Y300,$c:E400:E600,$c-$8-$3:E300:E200',
Overlay:'xs:X700-16:X700-16,sm:X700-24:X700-24,md:X700-40:X700-40,lg:X700-64:X700-64,xlg:X700-80:X700-80',
}
const SCP:Record<string,VariableScope[]>={Border:['STROKE_COLOR','FRAME_FILL','SHAPE_FILL'],Surface:['FRAME_FILL','SHAPE_FILL'],Action:['FRAME_FILL','SHAPE_FILL'],Text:['TEXT_FILL'],Icon:['FRAME_FILL','SHAPE_FILL','STROKE_COLOR'],Overlay:['FRAME_FILL','SHAPE_FILL']}
interface SemTok{name:string;d:string;l:string;sc:VariableScope[]}
const CS_T:SemTok[]=[]
for(const[g,s]of Object.entries(SEM_G))for(const i of expW(s).split(',')){const[nm,d,l]=i.split(':');CS_T.push({name:`${g}/${nm}`,d:PFX[d[0]]+d.slice(1),l:PFX[l[0]]+l.slice(1),sc:SCP[g]})}
const WK=['regular','medium','semibold']as const
const TS_T:Array<[string,number,number,'regular'|'medium'|'semibold']>=[
...[[60,60],[48,48],[36,40],[30,36],[24,32],[20,28]].map(([s,l],i)=>[`Heading / Heading ${i+1}`,s,l,'regular']as[string,number,number,'regular']),
...[[20,28],[18,28],[16,24],[14,20],[12,16]].map(([s,l],i)=>[`Title / Title ${i+1}`,s,l,'medium']as[string,number,number,'medium']),
...[[16,24,2],[16,24,1],[16,24,0],[14,20,2],[14,20,1],[14,20,0]].map(([s,l,w],i)=>[`Body / Body ${i+1}`,s,l,WK[w]]as[string,number,number,'regular'|'medium'|'semibold']),
...[['lg',18,28],['md',16,24],['sm',14,20],['Xsm',12,16]].map(([k,s,l])=>[`Button / Button-${k}`,s as number,l as number,'semibold']as[string,number,number,'semibold']),
...[[14,20,1],[14,20,0],[12,16,2],[12,16,1],[12,16,0]].map(([s,l,w],i)=>[`Caption / Caption ${i+1}`,s,l,WK[w]]as[string,number,number,'regular'|'medium'|'semibold']),
]
let T:Tok
function box(nm:string,dir:'VERTICAL'|'HORIZONTAL',p?:ChildrenMixin,gap=0,pad=0,fl?:Variable|string,st?:Variable|string,rd=0,fH=false):FrameNode{
const f=figma.createFrame()
f.name=nm;f.layoutMode=dir;f.primaryAxisSizingMode='AUTO';f.counterAxisSizingMode='AUTO';f.fills=[];f.clipsContent=false
if(T){
f.setBoundVariable('itemSpacing',T.sp[`spacing/${gap}`])
if(pad)bPad(f,T.sp[`spacing/${pad}`])
if(rd)bRad(f,T.rd[`radius/${rd === 9999 ? 'full' : rd}`])
}
bFill(f,typeof fl==='string'?T.cs[fl]||T.core[fl]:fl)
bStrk(f,typeof st==='string'?T.cs[st]||T.core[st]:st)
if(p){p.appendChild(f);if(fH)f.layoutSizingHorizontal='FILL'}
return f
}
async function txt(nm:string,ch:string,st:string|TextStyle,col:string|Variable,p?:ChildrenMixin,w?:number):Promise<TextNode>{
const t=figma.createText(),s=typeof st==='string'?T.ts[st]:st,c=typeof col==='string'?T.cs[col]||T.core[col]:col
t.name=nm;await t.setTextStyleIdAsync(s.id);t.characters=ch;bFill(t,c)
if(w){t.resize(w,24);t.textAutoResize='HEIGHT'}else t.textAutoResize='WIDTH_AND_HEIGHT'
if(p)p.appendChild(t)
return t
}
function mkIcon(nm:string,sz:number,col:string|Variable):FrameNode{
const c=typeof col==='string'?T.cs[col]:col
const n=figma.createNodeFromSvg(`<svg width="24" height="24" viewBox="0 0 24 24" fill="none"><path d="${ICONS[nm] || ICONS.SunCloud}" stroke="#FFF" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>`)
n.name=`Phosphor / ${nm}`;n.resize(sz,sz);n.fills=[]
n.findAll().forEach((ch,i)=>{ch.name=`${nm} ${i+1}`;if('strokes' in ch&&ch.strokes.length)bStrk(ch as GeometryMixin&MinimalStrokesMixin,c,2,'CENTER')})
const w=box(`Icon / ${nm}`,H);w.primaryAxisAlignItems='CENTER';w.counterAxisAlignItems='CENTER';w.appendChild(n);return w
}
interface Tok{sp:Record<string,Variable>;rd:Record<string,Variable>;core:Record<string,Variable>;cs:Record<string,Variable>;csA:Record<string,Variable>;coll:VariableCollection;dId:string;lId:string|null;ts:Record<string,TextStyle>}
async function setupTokens(theme:'Dark'|'Light'):Promise<Tok>{
const av=await figma.listAvailableFontsAsync(),inter=av.filter((f)=>f.fontName.family==='Inter').map((f)=>f.fontName.style)
const fam=inter.length?'Inter':av[0]?.fontName.family||'Inter',sts=fam==='Inter'?inter:av.filter((f)=>f.fontName.family===fam).map((f)=>f.fontName.style)
const reg=sts.includes('Regular')?'Regular':sts[0]||'Regular',med=sts.includes('Medium')?'Medium':reg,semi=sts.find((s)=>s==='Semi Bold'||s==='SemiBold'||s==='Bold')||med
const fts={regular:{family:fam,style:reg},medium:{family:fam,style:med},semibold:{family:fam,style:semi}}
await Promise.all([figma.loadFontAsync(fts.regular),figma.loadFontAsync(fts.medium),figma.loadFontAsync(fts.semibold)])
const colls=await figma.variables.getLocalVariableCollectionsAsync(),vars=await figma.variables.getLocalVariablesAsync()
const getC=(n:string)=>colls.find((c)=>c.name===n)||figma.variables.createVariableCollection(n)
const byC=(id:string)=>{const r:Record<string,Variable>={};for(const v of vars)if(v.variableCollectionId===id)r[v.name]=v;return r}
const mkF=(cN:string,ls:Array<[string,number]>,sc:VariableScope[])=>{
const c=getC(cN);c.renameMode(c.modes[0].modeId,'Value');const m=byC(c.id),o:Record<string,Variable>={}
for(const[n,val]of ls){const v=m[n]||figma.variables.createVariable(n,c,'FLOAT');v.scopes=sc;v.setValueForMode(c.modes[0].modeId,val);o[n]=v}
return o
}
const sp=mkF('spacing',SP_T,['GAP']),rd=mkF('radius',RD_T,['CORNER_RADIUS'])
const crC=getC('Core Colors');crC.renameMode(crC.modes[0].modeId,'Value');const crM=byC(crC.id),core:Record<string,Variable>={}
for(const[pN,shs]of Object.entries(CORE_P))for(const[sh,hx,a]of shs){
const k=`${pN}/${sh}`,v=crM[k]||figma.variables.createVariable(k,crC,'COLOR'),c=hx.slice(1),n=(i:number)=>parseInt(c.slice(i,i+2),16)/255
v.scopes=[];v.setValueForMode(crC.modes[0].modeId,{r:n(0),g:n(2),b:n(4),a});core[k]=v
}
const coll=getC('Color System'),pM=theme==='Dark'?'Dark':'Light',sM=theme==='Dark'?'Light':'Dark'
coll.renameMode(coll.modes[0].modeId,pM);const m1=coll.modes[0].modeId;let m2:string|null=null
if(coll.modes.length>1){m2=coll.modes[1].modeId;coll.renameMode(m2,sM)}else{try{m2=coll.addMode(sM)}catch{m2=null}}
const dId=theme==='Dark'?m1:m2||m1,lId=theme==='Light'?m1:m2,csM=byC(coll.id),cs:Record<string,Variable>={},csA:Record<string,Variable>={}
for(const s of CS_T){
const v=csM[s.name]||figma.variables.createVariable(s.name,coll,'COLOR');v.scopes=s.sc
const v1=core[theme==='Dark'?s.d:s.l],v2=core[theme==='Dark'?s.l:s.d]
if(v1)v.setValueForMode(m1,va(v1));if(m2&&v2)v.setValueForMode(m2,va(v2));cs[s.name]=v
}
if(!m2){
const aC=getC(`Color System (${sM})`);aC.renameMode(aC.modes[0].modeId,sM);const aM=byC(aC.id)
for(const s of CS_T){const v=aM[s.name]||figma.variables.createVariable(s.name,aC,'COLOR');v.scopes=[];const tg=core[sM==='Light'?s.l:s.d];if(tg)v.setValueForMode(aC.modes[0].modeId,va(tg));csA[s.name]=v}
}
const exS:Record<string,TextStyle>={},ts:Record<string,TextStyle>={}
for(const s of await figma.getLocalTextStylesAsync())exS[s.name]=s
for(const[n,sz,lh,wk]of TS_T){
const st=exS[n]||figma.createTextStyle();st.name=n;st.fontName=fts[wk];st.fontSize=sz;st.lineHeight={unit:'PIXELS',value:lh};st.letterSpacing={unit:'PIXELS',value:0};ts[n]=st
}
T={sp,rd,core,cs,csA,coll,dId,lId,ts}
return T
}
async function buildFoundations(page:PageNode,t:Tok):Promise<void>{
await figma.setCurrentPageAsync(page);for(const ch of[...page.children])ch.remove()
const root=box('Foundations',V,page,600,600,'Surface/Primary','Border/Primary',24)
await txt('Title','WeatherWardrobe Foundations','Heading / Heading 3','Text/Primary',root)
const sec=(nm:string,d:'VERTICAL'|'HORIZONTAL'=V,p=root)=>box(nm,d,p,100,200,'Surface/Primary-subtle','Border/Primary',16)
const cSec=sec('Core Colors')
for(const[pN,shs]of Object.entries(CORE_P)){
const rw=box(`Palette / ${pN}`,V,cSec,75);await txt('Label',pN,'Title / Title 4','Text/Secondary',rw)
const swR=box('Swatches',H,rw,75)
for(const[sh,hx]of shs){
const k=`${pN}/${sh}`,cd=box(`Swatch / ${k}`,V,swR,50,50,'Surface/Secondary','Border/Primary',8)
box('Sample',H,cd,0,0,k,undefined,4).resize(52,24)
await txt('Shade',`${sh} ${hx}`,'Caption / Caption 5','Text/Primary',cd)
}
}
const sSec=sec('Color System')
for(const grp of Object.keys(SEM_G)){
const cols=box(`Columns / ${grp}`,H,sSec,300)
const dC=box(`Dark / ${grp}`,V,cols,75,200,'Grayscale/870','Border/Primary',12)
const lC=box(`Light / ${grp}`,V,cols,75,200,'Grayscale/50','Border/Primary',12)
if(t.lId){dC.setExplicitVariableModeForCollection(t.coll,t.dId);lC.setExplicitVariableModeForCollection(t.coll,t.lId)}
for(const s of CS_T.filter((x)=>x.name.startsWith(`${grp}/`))){
for(const[col,v,al,txV]of[[dC,t.cs[s.name],s.d,'Grayscale/50'],[lC,t.lId?t.cs[s.name]:t.csA[s.name]||t.cs[s.name],s.l,'Grayscale/900']]as Array<[FrameNode,Variable,string,string]>){
const r=box(`Token / ${s.name}`,H,col,100,50);r.counterAxisAlignItems='CENTER'
box('Swatch',H,r,0,0,v,'Border/Tertiary',4).resize(20,20)
await txt('Label',`${s.name} → ${al}`,'Caption / Caption 4',txV,r)
}
}
}
const mRow=box('Metrics',H,root,400),spS=sec('Spacing',V,mRow),rdS=sec('Radius',H,mRow),tyS=sec('Typography')
for(const[n,val]of SP_T){
const r=box(`Row / ${n}`,H,spS,100,25);r.counterAxisAlignItems='CENTER'
await txt('Name',`${n} (${val}px)`,'Caption / Caption 3','Text/Secondary',r,130)
const b=box('Bar',H,r,0,0,'Action/Primary',undefined,4);b.resize(Math.max(2,val),12);if(val>0)b.setBoundVariable('width',t.sp[n])
}
for(const[n,val]of RD_T){
const c=box(`Radius / ${n}`,V,rdS,50,50);c.counterAxisAlignItems='CENTER'
box('Box',H,c,0,0,'Surface/Secondary','Border/Action-Primary',val).resize(44,44)
await txt('Label',`${n} (${val})`,'Caption / Caption 4','Text/Primary',c)
}
for(const[n,sz,lh]of TS_T){
const r=box(`Style / ${n}`,H,tyS,200,25);r.counterAxisAlignItems='CENTER'
await txt('Meta',`${n} (${sz}/${lh})`,'Caption / Caption 4','Text/Tertiary',r,210)
await txt('Sample','WeatherWardrobe Умный гардероб',n,'Text/Primary',r)
}
}
interface Comps{bSet:ComponentSetNode;bP:string;iSet:ComponentSetNode;iLP:string;iVP:string;card:ComponentNode;cTP:string;cMP:string;cBP:string;nSet:ComponentSetNode}
async function buildComponents(page:PageNode,t:Tok):Promise<Comps>{
await figma.setCurrentPageAsync(page);for(const ch of[...page.children])ch.remove()
const S=(k:number)=>t.sp[`spacing/${k}`],R=(k:number)=>t.rd[`radius/${k}`]
const wrap=box('Components',V,page,600,600,'Surface/Primary','Border/Primary',24)
const styleSet=(cs:ComponentSetNode,w:number)=>{
cs.layoutMode=H;cs.layoutWrap='WRAP';cs.resize(w,100);cs.primaryAxisSizingMode='FIXED';cs.counterAxisSizingMode='AUTO'
bPad(cs,S(300));cs.setBoundVariable('itemSpacing',S(200));cs.setBoundVariable('counterAxisSpacing',S(200))
bFill(cs,t.cs['Surface/Primary-subtle']);bStrk(cs,t.cs['Border/Action-Primary']);bRad(cs,R(16))
}
const bCs:ComponentNode[]=[]
for(const bt of['Primary','Secondary','Tertiary','Error','Warning']as const){
for(const bs of['Default','Hover','Pressed','Focus','Disabled']as const){
const c=figma.createComponent()
c.name=`Type=${bt}, State=${bs}`;c.layoutMode=H;c.primaryAxisSizingMode='AUTO';c.counterAxisSizingMode='AUTO';c.primaryAxisAlignItems='CENTER';c.counterAxisAlignItems='CENTER';c.minHeight=44
bPad(c,S(75),S(100));c.setBoundVariable('itemSpacing',S(75));bRad(c,R(8))
const fM:Record<string,string>={
Primary:bs==='Hover'?'Action/Primary-Hover':bs==='Pressed'?'Action/Primary-Click':bs==='Disabled'?'Action/Primary-Disabled':'Action/Primary',
Secondary:bs==='Hover'?'Action/Secondary-Hover':bs==='Pressed'?'Action/Secondary-Click':bs==='Disabled'?'Surface/Primary-subtle':'Action/Secondary',
Tertiary:bs==='Hover'?'Surface/Primary-Hover':bs==='Pressed'?'Surface/Primary-Selected':'Surface/Primary-subtle',
Error:bs==='Hover'||bs==='Pressed'?'Action/Error-Hover':bs==='Disabled'?'Action/Error-Disabled':'Action/Error',
Warning:bs==='Hover'||bs==='Pressed'?'Action/Warning - Hover':bs==='Disabled'?'Action/Warning - Disabled':'Action/Warning',
}
bFill(c,t.cs[fM[bt]])
if(bs==='Focus')bStrk(c,t.cs['Border/Action-Primary'],2,'OUTSIDE')
else if(bt==='Secondary'||bt==='Tertiary')bStrk(c,t.cs['Border/Primary'])
await txt('Label','Действие','Button / Button-md',bs==='Disabled'?'Text/Primary-Disabled':bt==='Warning'?'Text/Inverted':'Text/Primary',c)
page.appendChild(c);bCs.push(c)
}
}
const bSet=figma.combineAsVariants(bCs,wrap);bSet.name='Button';styleSet(bSet,1100)
const bP=bSet.addComponentProperty('Label','TEXT','Действие')
for(const ch of bSet.children){const tx=(ch as ComponentNode).findOne((n)=>n.type==='TEXT'&&n.name==='Label')as TextNode|null;if(tx)tx.componentPropertyReferences={characters:bP}}
const iCs:ComponentNode[]=[]
for(const is of['Default','Hover','Pressed','Focus','Disabled','Error','Filled']as const){
const c=figma.createComponent()
c.name=`State=${is}`;c.layoutMode=V;c.resize(320,72);c.primaryAxisSizingMode='AUTO';c.counterAxisSizingMode='FIXED';c.fills=[];c.setBoundVariable('itemSpacing',S(50))
await txt('Label','Поле ввода','Caption / Caption 1',is==='Error'?'Text/Error':'Text/Secondary',c)
const bx=box('Box',H,c,75,100,'Surface/Secondary',undefined,8,true);bx.minHeight=44;bx.counterAxisAlignItems='CENTER'
bStrk(bx,t.cs[is==='Focus'?'Border/Action-Primary':is==='Error'?'Border/Error':'Border/Primary'],is==='Focus'?2:1,is==='Focus'?'OUTSIDE':'INSIDE')
await txt('Value','student@mirea.ru','Body / Body 3','Text/Primary',bx)
page.appendChild(c);iCs.push(c)
}
const iSet=figma.combineAsVariants(iCs,wrap);iSet.name='Input';styleSet(iSet,1400)
const iLP=iSet.addComponentProperty('Label','TEXT','Поле ввода'),iVP=iSet.addComponentProperty('Value','TEXT','student@mirea.ru')
for(const ch of iSet.children){
const tl=(ch as ComponentNode).findOne((n)=>n.type==='TEXT'&&n.name==='Label')as TextNode|null
const tv=(ch as ComponentNode).findOne((n)=>n.type==='TEXT'&&n.name==='Value')as TextNode|null
if(tl)tl.componentPropertyReferences={characters:iLP}
if(tv)tv.componentPropertyReferences={characters:iVP}
}
const card=figma.createComponent()
card.name='Clothing Card';card.layoutMode=H;card.resize(358,88);card.primaryAxisSizingMode='FIXED';card.counterAxisSizingMode='AUTO';card.counterAxisAlignItems='CENTER'
bPad(card,S(75));card.setBoundVariable('itemSpacing',S(100));bFill(card,t.cs['Surface/Secondary']);bStrk(card,t.cs['Border/Primary']);bRad(card,R(16))
const th=box('Thumb',H,card,0,100,'Surface/Secondary-Subtle','Border/Primary',8);th.resize(56,56);th.primaryAxisAlignItems='CENTER';th.counterAxisAlignItems='CENTER'
th.appendChild(mkIcon('Jacket',24,'Icon/Info'))
const inf=box('Info',V,card,50,0,undefined,undefined,0,true)
const cTP=card.addComponentProperty('Title','TEXT','Мембранная ветровка Urban'),txT=await txt('Title','Мембранная ветровка Urban','Title / Title 4','Text/Primary',inf);txT.componentPropertyReferences={characters:cTP}
const cMP=card.addComponentProperty('Meta','TEXT','Верхняя одежда · от +8°C до +18°C'),txM=await txt('Meta','Верхняя одежда · от +8°C до +18°C','Caption / Caption 2','Text/Secondary',inf);txM.componentPropertyReferences={characters:cMP}
const bdg=box('Badge',H,inf,50,25,'Surface/Primary-subtle','Border/Action-Primary',8);bPad(bdg,S(25),S(75))
const cBP=card.addComponentProperty('Badge','TEXT','Подходит к +14°C'),txB=await txt('Badge','Подходит к +14°C','Caption / Caption 3','Text/Info',bdg);txB.componentPropertyReferences={characters:cBP}
wrap.appendChild(card)
const tabs=[['Catalog','Каталог','SunCloud'],['Wardrobe','Гардероб','Hanger'],['Scanner','Сканер','Scan'],['Profile','Профиль','User']]as const
const nVs:ComponentNode[]=[]
for(const[actId]of tabs){
const nc=figma.createComponent()
nc.name=`Tab=${actId}`;nc.layoutMode=V;nc.resize(390,68);nc.primaryAxisSizingMode='AUTO';nc.counterAxisSizingMode='FIXED';bFill(nc,t.cs['Surface/Primary-subtle']);nc.setBoundVariable('itemSpacing',S(0))
const dv=figma.createRectangle();dv.name='Divider';dv.resize(390,1);bFill(dv,t.cs['Border/Primary']);nc.appendChild(dv);dv.layoutSizingHorizontal='FILL'
const row=box('Row',H,nc,50,75,undefined,undefined,0,true)
for(const[tid,tLbl,tIc]of tabs){
const sel=tid===actId,it=box(`Tab / ${tid}`,V,row,25,50,sel?'Surface/Secondary':undefined,sel?'Border/Action-Primary':undefined,8,true)
it.counterAxisAlignItems='CENTER';it.primaryAxisAlignItems='CENTER';it.minHeight=48
it.appendChild(mkIcon(tIc,20,sel?'Icon/Info':'Icon/Subtle'))
await txt('Label',tLbl,sel?'Caption / Caption 3':'Caption / Caption 5',sel?'Text/Info':'Text/Tertiary',it)
}
page.appendChild(nc);nVs.push(nc)
}
const nSet=figma.combineAsVariants(nVs,wrap);nSet.name='Bottom Navigation';styleSet(nSet,1680)
return{bSet,bP,iSet,iLP,iVP,card,cTP,cMP,cBP,nSet}
}
type Op=['box',string,string,string]|['in',string,string]|['btn',string,string]|['card',string,string,string]
const SCR_S:Array<[string,string,string,string|undefined,Op[]]>=[
['Screen 01 / Авторизация','Вход в аккаунт','Вход',undefined,[
['box','Умный гардероб по погоде','Войдите в аккаунт для синхронизации личного гардероба и ежедневных рекомендаций','Border/Primary'],
['in','Электронная почта','student@mirea.ru'],['in','Пароль','••••••••••••'],
['btn','Primary','Войти в аккаунт'],['btn','Secondary','Создать новый аккаунт'],['btn','Tertiary','Продолжить как гость'],
]],
['Screen 02 / Каталог и погода','Каталог и погода','Москва','Catalog',[
['box','Москва · +14°C · Облачно (Ветер 4 м/с)','Рекомендация: прохладно, подойдут ветровка, худи и джинсы','Border/Action-Primary'],
['card','Мембранная ветровка Urban','Верхняя одежда · от +8°C до +18°C','Подходит к +14°C'],
['card','Оверсайз худи с начесом','Верх (Худи) · от +10°C до +20°C','В гардеробе'],
['card','Классические синие джинсы','Низ (Джинсы) · от 0°C до +22°C','В гардеробе'],
]],
['Screen 03 / Детали вещи','Детали вещи','О вещи','Catalog',[
['box','Мембранная ветровка Urban (+8°C..+18°C)','Легкая непродуваемая куртка с защитой от дождя и ветра до 12 м/с для прохладной погоды','Border/Primary'],
['btn','Primary','Добавить в мой гардероб'],
]],
['Screen 04 / Мой гардероб','Мой гардероб','3 вещи','Wardrobe',[
['card','Мембранная ветровка Urban','Добавлено сегодня · от +8°C до +18°C','В гардеробе'],
['card','Оверсайз худи с начесом','В избранном · от +10°C до +20°C','В гардеробе'],
['card','Классические синие джинсы','В избранном · от 0°C до +22°C','В гардеробе'],
['btn','Primary','Отсканировать вещь камерой'],
]],
['Screen 05 / Сканер одежды','Сканер одежды','Камера','Scanner',[
['box','Распознано: Оверсайз худи с начесом','Категория: Верх (Худи и свитшоты) · Комфортная температура от +10°C до +20°C','Border/Success'],
['btn','Primary','Сделать фото одежды'],['btn','Secondary','Сохранить вещь в гардероб'],
]],
['Screen 06 / Профиль','Профиль','Аккаунт','Profile',[
['box','student@mirea.ru','Статус: Личный гардероб синхронизирован','Border/Primary'],
['in','Город для прогноза погоды','Москва'],
['btn','Primary','Сохранить настройки'],['btn','Error','Выйти из аккаунта'],
]],
['State 01 / Загрузка каталога','Загрузка каталога','Обновление','Catalog',[['box','Загрузка каталога','Обновляем прогноз погоды и подбираем подходящие образы на сегодня','Border/Primary'],['btn','Primary','Обновить данные']]],
['State 02 / Пустой гардероб','Гардероб пока пуст','0 вещей','Wardrobe',[['box','Гардероб пока пуст','Добавьте подходящие по погоде вещи из каталога или сфотографируйте одежду камерой','Border/Primary'],['btn','Primary','Перейти в каталог']]],
['State 03 / Ошибка соединения','Нет соединения','Офлайн','Catalog',[['box','Ошибка соединения','Не удалось загрузить прогноз погоды, сохраненные вещи доступны в офлайн-режиме','Border/Error'],['btn','Primary','Повторить попытку']]],
]
async function buildScreens(page:PageNode,t:Tok,c:Comps):Promise<FrameNode[]>{
await figma.setCurrentPageAsync(page)
for(const ch of[...page.children])ch.remove()
const root=box('WeatherWardrobe / Design System',V,page,600,0)
const desk=box('WeatherWardrobe / Desktop Overview 1440',V,root,600,700,'Surface/Primary','Border/Primary',24)
desk.resize(1440,100);desk.counterAxisSizingMode='FIXED';desk.primaryAxisSizingMode='AUTO';desk.counterAxisAlignItems='CENTER'
const c1280=box('Container 1280',V,desk,400);c1280.resize(1280,100);c1280.counterAxisSizingMode='FIXED';c1280.primaryAxisSizingMode='AUTO'
const h1=await txt('Hero / Heading 1','Умный гардероб по погоде\nПерсональный подбор образов','Heading / Heading 1','Text/Primary',c1280,680)
h1.fills=[{type:'GRADIENT_LINEAR',gradientTransform:[[1,0,0],[0,1,0]],gradientStops:[
{position:0,color:{r:1,g:1,b:1,a:1},boundVariables:{color:va(t.cs['Text/Primary'])}},
{position:1,color:{r:0.6,g:0.6,b:0.6,a:1},boundVariables:{color:va(t.cs['Text/Tertiary'])}},
]}]
await txt('Hero / Subtitle','Ежедневные рекомендации одежды с учетом температуры, ветра и осадков в вашем городе, управление личным гардеробом и быстрое добавление вещей по фотографии','Body / Body 2','Text/Secondary',c1280,680)
const setT=(inst:InstanceNode,p:string,n:string,v:string)=>{
try{inst.setProperties({[p]:v})}catch{}
const tx=inst.findOne((x)=>x.type==='TEXT'&&x.name===n)as TextNode|null
if(tx)tx.characters=v
}
const sWrap=box('WeatherWardrobe / Mobile Screens (390x844)',H,root,400,600,'Surface/Primary','Border/Primary',24)
const stWrap=box('WeatherWardrobe / Состояния экранов',H,root,400,600,'Surface/Primary','Border/Primary',24)
for(const[nm,ti,bdg,navTab,ops]of SCR_S){
const par=nm.startsWith('State')?stWrap:sWrap
const sh=box(nm,V,par,0,0,'Surface/Primary','Border/Tertiary',24)
sh.resize(390,844);sh.primaryAxisSizingMode='FIXED';sh.counterAxisSizingMode='FIXED';sh.clipsContent=true
const sb=box('Status Bar',H,sh,200,75,'Surface/Primary-subtle',undefined,0,true);bPad(sb,t.sp['spacing/75'],t.sp['spacing/200']);sb.primaryAxisAlignItems='SPACE_BETWEEN'
await txt('Time','09:41','Caption / Caption 3','Text/Secondary',sb);await txt('Net','5G · 100%','Caption / Caption 3','Text/Secondary',sb)
const ab=box('App Bar',H,sh,100,200,'Surface/Secondary',undefined,0,true);bPad(ab,t.sp['spacing/100'],t.sp['spacing/200']);ab.primaryAxisAlignItems='SPACE_BETWEEN';ab.counterAxisAlignItems='CENTER'
await txt('Title',ti,'Title / Title 3','Text/Primary',ab);await txt('Module',bdg,'Caption / Caption 3','Text/Info',ab)
const dv=figma.createRectangle();dv.name='Divider';dv.resize(390,1);bFill(dv,t.cs['Border/Primary']);sh.appendChild(dv);dv.layoutSizingHorizontal='FILL'
const body=box('Content',V,sh,100,200,undefined,undefined,0,true);body.layoutSizingVertical='FILL'
for(const op of ops){
if(op[0]==='box'){
const cd=box('Card',V,body,75,200,'Surface/Secondary',op[3],16,true)
await txt('Title',op[1],'Title / Title 3','Text/Primary',cd,310);await txt('Desc',op[2],'Body / Body 6','Text/Secondary',cd,310)
}else if(op[0]==='in'){
const i=((c.iSet.children.find((x)=>x.name==='State=Filled')||c.iSet.defaultVariant)as ComponentNode).createInstance()
i.name=`Input / ${op[1]}`;body.appendChild(i);i.layoutSizingHorizontal='FILL'
setT(i,c.iLP,'Label',op[1]);setT(i,c.iVP,'Value',op[2])
}else if(op[0]==='btn'){
const i=((c.bSet.children.find((x)=>x.name===`Type=${op[1]}, State=Default`)||c.bSet.defaultVariant)as ComponentNode).createInstance()
i.name=`Button / ${op[2]}`;body.appendChild(i);i.layoutSizingHorizontal='FILL'
setT(i,c.bP,'Label',op[2])
}else if(op[0]==='card'){
const i=c.card.createInstance()
i.name=`Card / ${op[1]}`;body.appendChild(i);i.layoutSizingHorizontal='FILL'
setT(i,c.cTP,'Title',op[1]);setT(i,c.cMP,'Meta',op[2]);setT(i,c.cBP,'Badge',op[3])
}
}
if(navTab){
const nv=((c.nSet.children.find((x)=>x.name===`Tab=${navTab}`)||c.nSet.defaultVariant)as ComponentNode).createInstance()
nv.name='Bottom Navigation';sh.appendChild(nv);nv.layoutSizingHorizontal='FILL'
}
}
return[root]
}
figma.root.setRelaunchData({[PID]:'Rebuild WeatherWardrobe native design'})
figma.showUI(__html__,{width:W,height:360})
async function runBuild(msg:Extract<Msg,{type:'build-design-system'}>):Promise<void>{
try{
post('1/4 Creating tokens and text styles...')
await figma.loadAllPagesAsync()
const tok=await setupTokens(msg.theme)
if(msg.removeCodeLayer){
try{const cn=await figma.getNodeByIdAsync('1:3');if(cn&&'remove' in cn)cn.remove()}catch{}
for(const pg of figma.root.children)for(const ch of[...pg.children])if(ch.name==='Code')ch.remove()
}
const p0=figma.root.children.find((p)=>p.name==='Page 1');if(p0)p0.name='Screens'
const getP=(n:string)=>figma.root.children.find((p)=>p.name===n)||(()=>{const np=figma.createPage();np.name=n;return np})()
const fP=getP('Foundations'),cP=getP('Components'),sP=getP('Screens')
await Promise.all([fP.loadAsync(),cP.loadAsync(),sP.loadAsync()])
if(msg.buildFoundations){post('2/4 Building Foundations page...');await buildFoundations(fP,tok)}
let comps:Comps|null=null
if(msg.buildComponents||msg.buildScreens){post('3/4 Building Components page...');comps=await buildComponents(cP,tok)}
if(msg.buildScreens&&comps){
post('4/4 Building Screens page...')
const frames=await buildScreens(sP,tok,comps)
await figma.setCurrentPageAsync(sP);figma.currentPage.selection=frames;figma.viewport.scrollAndZoomIntoView(frames)
}
post('Done: created Foundations, Components, and Screens',false,true)
}catch(e){post(`Error: ${e instanceof Error ? e.message : String(e)}`,true,true)}
}
figma.ui.onmessage=async(msg:Msg)=>{
if(msg.type==='resize'){figma.ui.resize(W,Math.max(180,Math.min(900,Math.round(msg.height))));return}
if(msg.type==='build-design-system')await runBuild(msg)
}
void runBuild({type:'build-design-system',theme:'Dark',removeCodeLayer:true,buildFoundations:true,buildComponents:true,buildScreens:true})