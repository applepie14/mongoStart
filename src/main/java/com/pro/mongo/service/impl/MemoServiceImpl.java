package com.pro.mongo.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
		
		return result;
	}
	@Override
	public int insertMemo(String login_id, MultipartFile memoPhoto, Map<String, Object> params) throws Exception {
		
		
		// 1. 정보 저장
		MemoVO memo = new MemoVO();
		memo.setType((String)params.get("memoType"));
		memo.setTitle((String)params.get("memoTitle"));
		memo.setDesc((String)params.get("memoContent"));
		memo.setUser_id(login_id);
		
		// 2. 이미지 저장 이름 세팅 
		
		Query query = new Query(Criteria.where("user_id").is(login_id));
		query.addCriteria(Criteria.where("type").is(params.get("memoType")));
		query.limit(1);
		query.with(Sort.by(Sort.Direction.DESC, "img"));
		
		MemoVO vo = mongo.findOne(query, MemoVO.class, "blogs");
		if(memoPhoto != null) {
			// 1. 필요한 데이터 꺼내오기
			int pos = memoPhoto.getOriginalFilename().lastIndexOf( "." );
			String ext = memoPhoto.getOriginalFilename().substring( pos + 1 );
			
			String[] splits = vo.getImg().split("\\.");
			log.info("############################ {}", splits[0]);
			
			int cnt = Integer.parseInt(splits[0].substring(splits[0].length()-1, splits[0].length()));
			log.info("############################ {}", splits[0].substring(splits[0].length()-1, splits[0].length()));
			log.info("############################ {}", cnt);
			
			// 2. 이미지 저장
			BufferedImage image = ImageIO.read(memoPhoto.getInputStream());
			ImageIO.write(image, ext, new File(dataPath + makeImgName(login_id, cnt+1, ext)));

			memo.setImg(makeImgName(login_id, cnt+1, ext));
			
		}
		log.debug("# {}", mongo.insert(memo, "blogs"));
		
		Query query2 = new Query(Criteria.where("user_id").is(login_id));
		return (int) mongo.count(query2, "blogs");
	}
	String makeImgName(String id, int cnt, String ext) {
		return "memoImage_" + id + "_" + cnt + "." + ext;
	}
}
