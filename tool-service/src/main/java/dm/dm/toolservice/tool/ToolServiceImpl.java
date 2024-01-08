package dm.dm.toolservice.tool;

import dm.dm.toolservice.account.Account;
import dm.dm.toolservice.account.AccountRepository;
import dm.dm.toolservice.exception.EntityNotFoundException;
import dm.dm.toolservice.exception.NotHavePermissions;
import dm.dm.toolservice.tool.dto.ToolDto;
import dm.dm.toolservice.tool.dto.ToolDtoCreate;
import dm.dm.toolservice.tool.dto.ToolDtoUpdate;
import dm.dm.toolservice.tool.dto.ToolMapper;
import dm.dm.toolservice.tool.enums.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ToolServiceImpl implements ToolService {
    private final ToolRepository toolRepository;
    private final AccountRepository accountRepository;
    private final ToolMapper toolMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ToolDto> tools(String text, Pageable pageable) {
        log.info("ToolService: Get tools with search");
        if (text.isBlank()){
            return Collections.emptyList();
        }
        return toolRepository.search(text,pageable).stream()
                .map(toolMapper)
                .toList();
    }

    @Override
    public ToolDto getById(long id) {
        log.info("ToolService: find tool by id: " + id);
        return toolMapper.apply(getTool(id));
    }

    @Override
    public ToolDto addNewTool(ToolDtoCreate dto, UserDetails userDetails) {
        log.info("ToolService: Account with name:{} added new tool", userDetails.getUsername());
        var currentUser = getAccount(userDetails);
        Category category = Category.valueOf(dto.getCategory().toUpperCase());
        var entity = Tool.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(BigDecimal.valueOf(dto.getPrice()))
                .category(category)
                .isAvailable(true)
                .owner(currentUser)
                .build();

        return toolMapper.apply(toolRepository.save(entity));
    }


    @Override
    public ToolDto updateTool(long toolId, ToolDtoUpdate dto, UserDetails userDetails) {
        log.info("ToolService: Account with name:{} update tool with id:{}", userDetails.getUsername(), toolId);
        var tool = getTool(toolId);

        if (isNotOwner(userDetails, tool)) {
            throw new NotHavePermissions("You can't update, you are not the owner");
        }
        //Todo: расмотреть вариант передавать Map<String,Object>update итерироваться по нему и обновлять при помощи switch/case
        if (dto.getName() != null) {
            tool.setName(dto.getName());
        }
        if (dto.getPrice() != null) {
            tool.setPrice(BigDecimal.valueOf(dto.getPrice()));
        }
        if (dto.getDescription() != null) {
            tool.setDescription(dto.getDescription());
        }

        return toolMapper.apply(toolRepository.save(tool));
    }

    @Override
    public void delete(Long toolId, UserDetails userDetails) {
        log.info("ToolService: Account with name:{} delete tool with id:{}", userDetails.getUsername(), toolId);
        var tool = getTool(toolId);
        if (isNotOwner(userDetails, tool)) {
            throw new NotHavePermissions("You can't delete tool, you are not the owner");
        }
        toolRepository.delete(tool);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ToolDto> getToolsByOwner(Pageable pageable, long ownerId) {
        log.info("ToolService: get tools by owner with id: {}", ownerId);
        if (!accountRepository.existsById(ownerId)){
            throw new EntityNotFoundException(String.format("Account with id:%s not found", ownerId));
        }

        return toolRepository.findAllByOwnerId(ownerId, pageable).stream()
                .map(toolMapper)
                .toList();
    }


    private boolean isNotOwner(UserDetails userDetails, Tool tool) {
        return !tool.getOwner().getEmail().equals(userDetails.getUsername());
    }

    private Account getAccount(UserDetails userDetails) {
        return accountRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Account with email:%s not found", userDetails.getUsername())));
    }

    private Tool getTool(long id) {
            return toolRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Tool with id:%s not found", id)));
    }
}
