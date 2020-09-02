import React, { useEffect, useState } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './questionnaire.reducer';
import { IQuestionnaire } from 'app/shared/model/questionnaire.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import {
  BarChart, Bar, Cell, XAxis, YAxis, CartesianGrid, Tooltip, Legend,
} from 'recharts';
import { PieChart, Pie, Sector } from 'recharts';
import question from '../question/question';
import { countBy } from 'lodash';
import _ from 'lodash';

export interface IQuestionnaireDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const QuestionnaireDetail = (props: IQuestionnaireDetailProps) => {
  const { questionnaireEntity } = props;
  const  [bool, setBool] = useState(0);
  const [ pr  , setPr] = useState([]);
 
  const [ group  , setGroup] = useState([]);
   
  const [ bars  , setBars] = useState([]);
  const [ group2  , setGroup2] = useState([]);


  useEffect(() => {
    const prv = [];  
     const  barsv=[];
     let mm = 0
    props.getEntity(props.match.params.id);
    questionnaireEntity.patientQuestionnaires? questionnaireEntity.patientQuestionnaires.map( pq => {
      
      pq.patientReponses.map ( p => {
         p.questionAnswer? 
       (prv.push( {'name' :p.question.label,  'choix' :p.questionAnswer.label }) , mm++,
       !barsv.some( prop => prop.data === p.questionAnswer.label  ) ? barsv.push( { 'data' : p.questionAnswer.label ,  color : "#000000".replace(/0/g,function(){return (~~(Math.random()*16)).toString(16) }) }) :null
       )
       :null;
      })  
      }) : null
      setBool(mm)

     
      setPr(prv)
      setBars(barsv)
      const groupv = prv.map((item)=>  item.name ).filter((item, i, ar) => ar.indexOf(item) === i).sort((a, b)=> a - b).map(item=>{
        const newList = prv.filter(itm => itm.name === item).map(itm=>itm.choix);
        return {name:item , ...countBy(newList)}
    });
    setGroup(groupv);
      

       const group2v = prv.map((item)=>  item.name ).filter((item, i, ar) => ar.indexOf(item) === i).sort((a, b)=> a - b).map(item=>{
        const newList = prv.filter(itm => itm.name === item).map(itm=>itm.choix);
       
        return [... _.map(_.countBy(newList), (val, key) => ({ name: key, value: val }))]
      });
      setGroup2(group2v)
             
  }, [questionnaireEntity.id]);

 
 

 

 


    
      
   
    const renderActiveShape = (pops) => {
      const RADIAN = Math.PI / 180;
      const {
        cx, cy, midAngle, innerRadius, outerRadius, startAngle, endAngle,
        fill, payload, percent, value,
      } = pops;
      const sin = Math.sin(-RADIAN * midAngle);
      const cos = Math.cos(-RADIAN * midAngle);
      const sx = cx + (outerRadius + 10) * cos;
      const sy = cy + (outerRadius + 10) * sin;
      const mx = cx + (outerRadius + 30) * cos;
      const my = cy + (outerRadius + 30) * sin;
      const ex = mx + (cos >= 0 ? 1 : -1) * 22;
      const ey = my;
      const textAnchor = cos >= 0 ? 'start' : 'end';
    
      return (
        <g>
          <text x={cx} y={cy} dy={8} textAnchor="middle" fill={fill}>{payload.name}</text>
          <Sector
            cx={cx}
            cy={cy}
            innerRadius={innerRadius}
            outerRadius={outerRadius}
            startAngle={startAngle}
            endAngle={endAngle}
            fill={fill}
          />
          <Sector
            cx={cx}
            cy={cy}
            startAngle={startAngle}
            endAngle={endAngle}
            innerRadius={outerRadius + 6}
            outerRadius={outerRadius + 10}
            fill={fill}
          />
          <path d={`M${sx},${sy}L${mx},${my}L${ex},${ey}`} stroke={fill} fill="none" />
          <circle cx={ex} cy={ey} r={2} fill={fill} stroke="none" />
          <text x={ex + (cos >= 0 ? 1 : -1) * 12} y={ey} textAnchor={textAnchor} fill="#333">{`NB ${value}`}</text>
          <text x={ex + (cos >= 0 ? 1 : -1) * 12} y={ey} dy={18} textAnchor={textAnchor} fill="#999">
            {`(Rate ${(percent * 100).toFixed(2)}%)`}
          </text>
        </g>
      );
    };
  
    const  [activeIndex, setAcIndex] = useState(0);
   
    
    const onPieEnter = (d, index) => {
      setAcIndex (index)
    };

  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="tnTsuiviApp.questionnaire.detail.title">Questionnaire</Translate> [<b>{questionnaireEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="subject">
              <Translate contentKey="tnTsuiviApp.questionnaire.subject">Subject</Translate>
            </span>
          </dt>
          <dd>{questionnaireEntity.subject}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="tnTsuiviApp.questionnaire.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={questionnaireEntity.startDate} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="tnTsuiviApp.questionnaire.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={questionnaireEntity.endDate} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            
            <Translate contentKey="tnTsuiviApp.questionnaire.question">Question</Translate>
          </dt>
           <dt>
          
            
           </dt>
          
           
            
          <dt>
        {// questionnaireEntity.patientQuestionnaires? questionnaireEntity.patientQuestionnaires[0].patientReponses[0]   :null}
 } </dt>
          <dd>
            {questionnaireEntity.questions
              ? questionnaireEntity.questions.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.label}</a>
                  
                    {i === questionnaireEntity.questions.length - 1 ? '' : ', '}
                  </span>
                )) 
              : null}
          </dd>
         
        </dl>
        {bool >0 ? (        <BarChart
        width={500}
        height={300}
        data={group}
        margin={{
          top: 5, right: 30, left: 20, bottom: 5,
        }}
      >
      <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="name" />
        <YAxis />
        <Tooltip />
      
       
        
        {  bars.map(bar => { return(
            <Bar key={Bar.data} dataKey={bar.data}  fill={bar.color} />)
        })
        }
 
    
      </BarChart> ):null}
           {bool >0 ? ( group2.map ( (item ,i)  =>  {   return ( 
                <div key = {i}> 
                <span>Question {i+1}</span>
               <PieChart key ={item.indexOf} width={400} height={400}>
               <Pie
                 activeIndex={activeIndex}
                 activeShape={renderActiveShape}
                 data={item}
                 cx={200}
                 cy={200}
                 innerRadius={60}
                 outerRadius={80}
                 fill="#8884d8"
                 dataKey="value"
                 onMouseEnter={onPieEnter}
               />
             </PieChart>  
             </div>
           )} ) ):null }
  
     
      

        <Button tag=
        {Link} to="/questionnaire" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        
        <Button tag={Link} to={`/questionnaire/${questionnaireEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      
      </Col>
    
    </Row>
  );
};

const mapStateToProps = ({ questionnaire }: IRootState) => ({
  questionnaireEntity: questionnaire.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(QuestionnaireDetail);
