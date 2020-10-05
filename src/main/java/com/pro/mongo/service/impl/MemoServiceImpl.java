package com.pro.mongo.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.pro.mongo.service.MemoService;
import com.pro.mongo.vo.MemoVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemoServiceImpl implements MemoService {
	private final MongoTemplate mongo;

	@Value("${mongoboard.data-path}")
	private String dataPath;

	@Override
	public Map<String, Object> getMyMemo(String login_id) {
		Query query = new Query(Criteria.where("user_id").is(login_id));

		List<MemoVO> memos = mongo.find(query, MemoVO.class, "blogs");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("memos", memos);
		
		for(MemoVO memo : memos) {
			memo.setObjectId(memo.get_id().toString());
		}
		
		return result;
	}
	@Override
	public int insertMemo(String login_id, MultipartFile memoPhoto, Map<String, Object> params) throws Exception {
		// 1. 정보 저장
		MemoVO memo = new MemoVO();
		memo.set_id(new ObjectId());
		memo.setType((String)params.get("memoType"));
		memo.setTitle((String)params.get("memoTitle"));
		memo.setDesc((String)params.get("memoContent"));
		memo.setUser_id(login_id);
		
		// 2. 이미지 저장 이름 세팅
		Query query = new Query(Criteria.where("user_id").is(login_id));
		query.addCriteria(Criteria.where("type").is(params.get("memoType"))).limit(1).with(Sort.by(Sort.Direction.DESC, "img"));
		
		MemoVO vo = mongo.findOne(query, MemoVO.class, "blogs");
		
		if("photo".equals(memo.getType()) && memoPhoto != null) {
			// 1. 필요한 데이터 꺼내오기
			int pos = memoPhoto.getOriginalFilename().lastIndexOf( "." );
			String ext = memoPhoto.getOriginalFilename().substring( pos + 1 );

			int cnt = 0;
			if(vo != null && vo.getImg() != null) {
				String[] splits = vo.getImg().split("\\.");
				cnt = Integer.parseInt(splits[0].substring(splits[0].length()-1, splits[0].length()));
			}
			// 2. 이미지 저장
			BufferedImage image = ImageIO.read(memoPhoto.getInputStream());
			ImageIO.write(image, ext, new File(dataPath + makeImgName(login_id, memo.get_id(), ext)));

			memo.setImg(makeImgName(login_id, memo.get_id(), ext));
		}
		log.debug("# {}", mongo.insert(memo, "blogs"));
		Query query2 = new Query(Criteria.where("user_id").is(login_id));
		return (int) mongo.count(query2, "blogs");
	}
	
	@Override
	public int updateMemo(String login_id, MultipartFile memoPhoto, Map<String, Object> params) throws Exception {
		// 1. update 구문
		Query query = new Query(Criteria.where("_id").is(new ObjectId((String)params.get("memoObjectId"))));
		Update update = new Update().set("title", (String)params.get("memoTitle")).set("type", (String)params.get("memoType"))
									.set("desc", (String)params.get("memoContent")).set("user_id", login_id);
		
		// 2. 이미지 저장 이름 세팅
		MemoVO vo = mongo.findOne(query, MemoVO.class, "blogs");
		
		if("photo".equals((String)params.get("memoType")) && memoPhoto != null) {
			// 2-1. 필요한 데이터 꺼내오기
			String ext = memoPhoto.getOriginalFilename().substring( memoPhoto.getOriginalFilename().lastIndexOf( "." ) + 1 );
			String[] splits = vo.getImg().split("\\.");
			
			// 2-2. 이미지 저장
			BufferedImage image = ImageIO.read(memoPhoto.getInputStream());
			ImageIO.write(image, ext, new File(dataPath + makeImgName(login_id, vo.get_id(), ext)));

			update.set("img", makeImgName(login_id, vo.get_id(), ext) );
			
		}else if(!"photo".equals((String)params.get("memoType")) && memoPhoto == null){
			if(vo != null && vo.getImg() != null) {
				File deleteImg = new File(dataPath + vo.getImg());
				deleteImg.delete();
			}
			update.set("img","");
		}
		
		UpdateResult msg = mongo.upsert(query, update, "blogs");
		log.debug("# {}", msg);
		
		return (int) msg.getModifiedCount();
	}
	
	@Override
	public DeleteResult deleteMemo(String login_id, String params) throws Exception {
		Query query = new Query(Criteria.where("_id").is(new ObjectId(params)));
		DeleteResult msg = mongo.remove(query, "blogs");
		return msg;
	}
	String makeImgName(String id, ObjectId _id, String ext) {
		return "memoImage_" + id + "_" + _id.toString() + "." + ext;
	}
}
