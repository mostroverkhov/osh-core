/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.sensorhub.impl.sensor;

import net.opengis.swe.v20.DataBlock;
import org.sensorhub.api.sensor.ISensorModule;
import org.sensorhub.api.sensor.SensorDataEvent;
import org.sensorhub.api.sensor.SensorException;
import org.vast.swe.SWEConstants;
import org.vast.swe.helper.GeoPosHelper;


/**
 * <p>
 * Default WGS84 location output with latitude, longitude, altitude coordinates.
 * Use of this class is highly recommended even for static sensors.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @param <SensorType> 
 * @since May 19, 2015
 */
public class DefaultLocationOutputLLA<SensorType extends ISensorModule<?>> extends DefaultLocationOutput<SensorType>
{
    
    public DefaultLocationOutputLLA(SensorType parentSensor, double updatePeriod)
    {
        super(parentSensor, updatePeriod);
    }

    
    @Override
    protected void init() throws SensorException
    {
        GeoPosHelper fac = new GeoPosHelper();
        outputStruct = fac.newLocationVectorLLA(SWEConstants.DEF_SENSOR_LOC);
        outputEncoding = fac.newTextEncoding();
    }
    

    @Override
    protected void updateLocation(double time, double x, double y, double z)
    {
        // build new datablock
        DataBlock dataBlock = (latestRecord == null) ? outputStruct.createDataBlock() : latestRecord.renew();
        dataBlock.setDoubleValue(0, time);
        dataBlock.setDoubleValue(1, y);
        dataBlock.setDoubleValue(2, x);
        dataBlock.setDoubleValue(3, z);
        
        // update latest record and send event
        latestRecord = dataBlock;
        latestRecordTime = System.currentTimeMillis();
        eventHandler.publishEvent(new SensorDataEvent(latestRecordTime, this, dataBlock));
    }

}
