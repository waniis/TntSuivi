import './footer.scss';

import React from 'react';
import { Translate } from 'react-jhipster';
import { Col, Row } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faMapMarkedAlt, faEnvelope ,faPhoneAlt} from '@fortawesome/free-solid-svg-icons';
import centre from 'app/entities/centre/centre';

const Footer = props => (
  <div className="footer page-content">
    <Row>
      <Col md="12">
        <p>
          <div style ={{display: 'flex',  justifyContent:'center', alignItems:'center'}}> 
          <span ><FontAwesomeIcon icon={faMapMarkedAlt} /> Tunisie Nouvelles Technologies  Imm. Panorama Centre Urbain Nord    </span>
    <span ><FontAwesomeIcon icon={faEnvelope} /> contact@tnt.com.tn   </span>
    <span ><FontAwesomeIcon icon={faPhoneAlt} /> (216) 71 948 087   </span>
    <span ><FontAwesomeIcon icon={faPhoneAlt} /> (216) 71 948 157  </span>


 </div>   

 <div style ={{display: 'flex',  justifyContent:'center', alignItems:'center'}}> 
 Tunisie Nouvelles Technologies © 2020
 </div>
       
        </p>
      </Col>
    </Row>
  </div>
);

export default Footer;
 